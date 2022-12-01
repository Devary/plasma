package services.processing.data;

import hierarchy.Classes.JavaClass;
import hierarchy.Classes.types.Function;
import hierarchy.Classes.types.JavaField;
import hierarchy.persistence.Persistent;
import hierarchy.persistence.types.Link;
import hierarchy.property.PropertiesFile;
import org.apache.log4j.Logger;
import services.IServiceImpl;
import services.processing.PropertyFileCreationProcess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class LoaderService extends IServiceImpl {

    private HashMap<String, String> mergedProperties = new HashMap<>();
    private HashMap<String, String> linkToPersistent = new HashMap<>();
    private ArrayList<Link> links = new ArrayList<>();
    private ArrayList<JavaClass> javaclasses = new ArrayList<>();
    private ArrayList<JavaClass> parsedJavaclasses = new ArrayList<>();
    private ArrayList<Persistent> persistences = new ArrayList<>();
    private ArrayList<JavaField> javaFields = new ArrayList<>();
    private ArrayList<Function> javaMethods = new ArrayList<>();
    private Connection conn = null;
    private Connections connectionService = new Connections();
    private SeederService seederService = new SeederService();
    private static Logger logger = Logger.getLogger(PropertyFileCreationProcess.class);


    public LoaderService(boolean resetConnection) {
        this.conn = connectionService.connect("",resetConnection);

        initStep();
    }

    private void initLinks(Persistent persistent) {
        logger.info("Initializing Links from Database");
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM LINKS WHERE persistent_id = "+persistent.getId());
            while (resultSet.next()) {
                Link link = new Link();
                link.setId(resultSet.getInt("id"));
                link.setName(resultSet.getString("name"));
                link.setDbtype(resultSet.getString("dbtype"));
                link.setDbname(resultSet.getString("dbname"));
                link.getIsCollection(resultSet.getBoolean("is_collection"));
                link.setElementType(resultSet.getString("elementType"));
                link.setInverseName(resultSet.getString("inverseName"));
                link.setAllowNulls(resultSet.getBoolean("allowsNull"));
                link.setPersistentId(resultSet.getLong("persistent_id"));
                persistent.addLink(link);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void initPersistents() {
        logger.info("Initializing Persistent from Database");

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PERSISTENT WHERE 1=1");
            while (resultSet.next()) {
                Persistent persistent = Persistent.newPersistent()
                        .name(resultSet.getString("name"))
                        .mappingType(resultSet.getString("mapping"))
                        .shortTableName(resultSet.getString("short_table_name"))
                        .table(resultSet.getString("table"))
                        .id(resultSet.getLong("id"))
                        .isPersistent(resultSet.getBoolean("is_persistent"))
                        .build();
                persistences.add(persistent);
                initLinks(persistent);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void initJavaMethods() {
        logger.info("Initializing Methods from Database");

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id,name,is_void,result_type,class_id FROM JAVA_METHOD WHERE 1=1");
            while (resultSet.next()) {
                Function method = Function.newFunction()
                        .name(resultSet.getString(2))
                        .isVoid(resultSet.getBoolean(3))
                        .resultType(resultSet.getString(4))
                        .classId(resultSet.getInt(5))
                        .id(resultSet.getInt(1)).build();
                javaMethods.add(method);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void assignPersistentsToJavaClasses() {
        logger.info("Assigning Persistents To Java Classes");

        for (JavaClass javaClass : parsedJavaclasses) {
            for (Persistent per : persistences) {
                if (javaClass.getClassName().equals(per.getName()) && javaClass.getClassType().equals("CLASS")
                        && (per.getLinks() == null && per.getFields() == null && per.getCodes() == null)
                        && javaClass.getJavaFields().size() == (per.getLinks().size() + per.getFields().size() + per.getCodes().size())) {
                    javaClass.setPersistent(per);
                }
            }
        }
    }

    private void assignMethodsToJavaClass() {
        logger.info("Assigning Methods To Java Classes");

        javaclasses.stream().forEach(javaClass -> {
            ArrayList<Function> linkedFields = (ArrayList<Function>) javaMethods.stream().filter(field -> {
                return field.getClass_id() == javaClass.getId();
            }).collect(Collectors.toList());
            javaClass.setFunctions(linkedFields);
        });
    }

    private void assignLinksToPersistents() {
        logger.info("Assigning Links To Persistents");

        persistences.stream().forEach(persistent -> {
            ArrayList<Link> linkedLinks = (ArrayList<Link>) links.stream().filter(link -> {
                return link.getPersistentId() == persistent.getId();
            }).collect(Collectors.toList());
            persistent.setLinks(linkedLinks);
        });
    }

    private void assignFieldsToJavaClass() {
        logger.info("Assigning Fields To Java C lasses");

        javaclasses.stream().forEach(javaClass -> {
            ArrayList<JavaField> linkedFields = (ArrayList<JavaField>) javaFields.stream().filter(field -> {
                return field.getClassId() == javaClass.getId();
            }).collect(Collectors.toList());
            javaClass.setJavaFields(linkedFields);
        });
    }

    private void initJavaClasses() {
        logger.info("Initializing Java Classes from Database ");

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM JAVA_CLASS WHERE 1=1");
            while (resultSet.next()) {
                JavaClass clazz = JavaClass.newJavaClass()
                        .className(resultSet.getString("className"))
                        .classType(resultSet.getString("classType"))
                        .implementations(extractClasses(resultSet.getString("implements")))
                        .heritances(extractClasses(resultSet.getString("inherit")))
                        .id(resultSet.getInt("id"))
                        .build();
                javaclasses.add(clazz);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void initJavaField() {
        logger.info("Initializing Fields from Database ");

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id,name,type,is_collection,collection_type,class_id FROM JAVA_FIELD WHERE 1=1");
            while (resultSet.next()) {
                JavaField field = new JavaField(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getBoolean(4),
                        resultSet.getString(5),
                        resultSet.getInt(6)
                );
                javaFields.add(field);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    private void initStep(){
        logger.info("Starting Initialization Step : ");

        boolean reset = true;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PERSISTENT FETCH FIRST 1 ROWS ONLY");
            while (resultSet.next()){
                reset = false;
                break;
            }
            if (reset){
                seederService.processorEntities();
            }


            //initLinks();
            initPersistents();
            //initJavaField();
            //initJavaMethods();
            //initJavaClasses();
            //assignPersistentsToJavaClasses();
            //assignFieldsToJavaClass();
            //assignMethodsToJavaClass();
            //assignLinksToPersistents();

            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void mergeProperties(ArrayList<PropertiesFile> properties) {
        for (PropertiesFile props : properties) {
            Properties prp = props.getProperties();
            for (Map.Entry property : prp.entrySet()) {
                mergedProperties.put((String) property.getValue(), (String) property.getKey());
            }
        }
    }

    private ArrayList<JavaClass> extractClasses(String statement) {
        ArrayList<JavaClass> classes = new ArrayList<>();
        if (statement == null) {
            return null;
        }
        String[] splits = statement.split(",");
        for (String split : splits) {
            classes.add(JavaClass.newJavaClass().className(split).build());
        }
        return classes;
    }



    //getters & Setters


    public HashMap<String, String> getMergedProperties() {
        return mergedProperties;
    }

    public void setMergedProperties(HashMap<String, String> mergedProperties) {
        this.mergedProperties = mergedProperties;
    }

    public HashMap<String, String> getLinkToPersistent() {
        return linkToPersistent;
    }

    public void setLinkToPersistent(HashMap<String, String> linkToPersistent) {
        this.linkToPersistent = linkToPersistent;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public ArrayList<JavaClass> getJavaclasses() {
        return javaclasses;
    }

    public void setJavaclasses(ArrayList<JavaClass> javaclasses) {
        this.javaclasses = javaclasses;
    }

    public ArrayList<JavaClass> getParsedJavaclasses() {
        return parsedJavaclasses;
    }

    public void setParsedJavaclasses(ArrayList<JavaClass> parsedJavaclasses) {
        this.parsedJavaclasses = parsedJavaclasses;
    }

    public ArrayList<Persistent> getPersistences() {
        return persistences;
    }

    public void setPersistences(ArrayList<Persistent> persistences) {
        this.persistences = persistences;
    }

    public ArrayList<JavaField> getJavaFields() {
        return javaFields;
    }

    public void setJavaFields(ArrayList<JavaField> javaFields) {
        this.javaFields = javaFields;
    }

    public ArrayList<Function> getJavaMethods() {
        return javaMethods;
    }

    public void setJavaMethods(ArrayList<Function> javaMethods) {
        this.javaMethods = javaMethods;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Connections getConnectionService() {
        return connectionService;
    }

    public void setConnectionService(Connections connectionService) {
        this.connectionService = connectionService;
    }

    public SeederService getSeederService() {
        return seederService;
    }

    public void setSeederService(SeederService seederService) {
        this.seederService = seederService;
    }
}
