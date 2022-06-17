package services.processing;

import Engines.PlasmaObjectTableName;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaType;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameterizedType;
import com.thoughtworks.qdox.model.impl.JavaMethodDelegate;
import files.FileTypes;
import hierarchy.Classes.JavaClass;
import hierarchy.Classes.types.JavaField;
import hierarchy.persistence.Persistent;
import hierarchy.persistence.types.Link;
import hierarchy.property.PropertiesFile;
import mappers.AbstractMapper;
import org.json.Property;
import projects.ProjectImpl;
import services.reporting.Report;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class VirtualLinkCreationProcess {
    private ArrayList<Link> links = new ArrayList<>();
    private ArrayList<JavaClass> javaclasses = new ArrayList<>();
    private ArrayList<JavaClass> parsedJavaclasses = new ArrayList<>();
    private ArrayList<Persistent> persistences = new ArrayList<>();
    private ArrayList<JavaField> javaFields = new ArrayList<>();
    public Map<Long, Long> virtualLinksPoints = new HashMap<>();
    public ArrayList<String> virtualLinksPointsV2 = new ArrayList<>();


    private Connection conn = null;
    private final String url = "jdbc:postgresql://localhost:5432/test";
    private final String user = "postgres";
    private final String password = "admin";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect(String driver, boolean reset) {
        if (conn == null || reset) {
            try {
                conn = null;
                if (driver.equals("ORA")) {
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@10.6.140.67:1521:orcl", "CLV61PERF22", "CLV61PERF22");
                } else {
                    conn = DriverManager.getConnection(url, user, password);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return conn;

    }


    public VirtualLinkCreationProcess(ArrayList<JavaClass> parsedJavaclasses) {
        this.parsedJavaclasses = parsedJavaclasses;
        connect("", false);
        initLinks();
        initPersistents();
        initJavaClasses();
        initJavaFields();
        assignFieldsToJavaClass();
        assignLinksToPersistents();
    }

    private void assignLinksToPersistents() {
        persistences.stream().forEach(persistent -> {
            ArrayList<Link> linkedLinks = (ArrayList<Link>) links.stream().filter(link -> {
                return link.getPersistentId() == persistent.getId();
            }).collect(Collectors.toList());
            persistent.setLinks(linkedLinks);
        });
    }

    private void assignFieldsToJavaClass() {
        javaclasses.stream().forEach(javaClass -> {
            ArrayList<JavaField> linkedFields = (ArrayList<JavaField>) javaFields.stream().filter(field -> {
                return field.getClassId() == javaClass.getId();
            }).collect(Collectors.toList());
            javaClass.setJavaFields(linkedFields);
        });
    }

    private void initJavaFields() {
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

    private void initJavaClasses() {
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

    private void initPersistents() {
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
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void initLinks() {
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM LINKS WHERE 1=1");
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
                links.add(link);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public VirtualLinkCreationProcess() {
    }

    public static void main(String[] args) {
        //doProcess(virtualLinkCreationProcess);

        VirtualLinkCreationProcess vcp = new VirtualLinkCreationProcess();
        //vcp.convertToUnderscoredName("endorsementOid");
        //vcp.convertToUnderscoredName("endorsementCID");
        //vcp.startGlobal();
        vcp.doProcess();
    }


    public void process() {
        //do process
        for (Link link : links) {
            try {
                //1.first step : create unique-linked persistent Virtual Link

                /*String inverseName = link.getInverseName();
                inverseName = inverseName.substring(0, 1).toUpperCase() + inverseName.substring(1);
                String finalInverseName = inverseName;
                Persistent persistent = persistences.stream().filter(per -> {
                    return per.getName().equals(finalInverseName);
                }).findFirst().orElse(null);

                if (persistent == null) {
                    String linkName = link.getName();
                    linkName = linkName.substring(0, 1).toUpperCase() + linkName.substring(1);
                    String finalLinkName = linkName;
                    persistent = persistences.stream().filter(per -> {
                        return per.getTable() != null && per.getTable().equals(finalLinkName);
                    }).findFirst().orElse(null);
                    String s = inverseName.substring(inverseName.length() - 1);
                    boolean isInverseNameInPlural = s.equals("s");
                    if (persistent == null && isInverseNameInPlural) {
                        inverseName = inverseName.substring(0, inverseName.length() - 1);
                        String finalInverseName1 = inverseName;
                        persistent = persistences.stream().filter(per -> {
                            return per.getName().equals(finalInverseName1);
                        }).findFirst().orElse(null);
                    }
                }

                if (persistent != null) {
                    virtualLinksPoints.put(persistent.getId(), link.getId());
                    continue;
                }*/

                //2. second step : get all persistent classes from INTERFACES and ABSTRACT classes
                JavaClass theClazz = null;
                String[] splits;
                if (link.getElementType() != null) {
                    splits = link.getElementType().split("\\.");
                    String name = splits[splits.length - 1];
                    theClazz = javaclasses.stream().filter(clazz -> {
                        return clazz.getClassName().equals(name);
                    }).findFirst().orElse(null);
                }

                ArrayList<JavaClass> subClasses = new ArrayList<>();
                if (theClazz != null) {
                    //tryToGetSubClasses(theClazz, link);
                    newAlgo(theClazz, link);

                    /*
                    if (theClazz.getClassType().equals("INTERFACE")) {
                        //get sub classes & check for sub interfaces
                        tmpSubClasses.addAll(getSubClasses(theClazz));
                    } else {
                        //check if there is a persistent with class name
                        persistent = persistences.stream().filter(per -> {
                            return per.getName().equals(theClazz.getClassName());
                        }).findFirst().orElse(null);
                        if (persistent == null) {
                            //find sub classes

                        } else {
                            //add persistent
                            virtualLinksPoints.put(persistent.getId(), link.getId());
                            subClasses.add(theClazz);
                        }
                    }*/

                }

                //3. create a link for each persistent


                //4. add to collection

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //fillVirtualLinksTable(link);
        //virtual link  => id ,
        // source,
        // target ,
        // from ( persistent's link owner) ,
        // to ( link's persistent target ) ,
        // text (iscollection),
        // to text,
        // name (link name),
        // inversename (link inverse name)
    }

    private void tryToGetSubClasses(JavaClass theClazz, Link link) {
        try {
            //ArrayList<JavaClass> tmpSubClasses = new ArrayList<>();
            subClazzz = new ArrayList<>();
            getSubClasses(theClazz);
            for (JavaClass jc : subClazzz
            ) {
                Persistent lclpers = persistences.stream().filter(per -> {
                    return per.getName().equals(jc.getClassName());
                }).findFirst().orElse(null);
                if (lclpers != null) {
                    virtualLinksPoints.put(lclpers.getId(), link.getId());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private ArrayList<JavaClass> subClazzz = new ArrayList<>();

    private ArrayList<JavaClass> getSubClasses(JavaClass theClazz) {
        //ArrayList<JavaClass> subClazzz = new ArrayList<>();
        if (theClazz.getImplementations() != null || theClazz.getHeritances() != null) {
            for (JavaClass clazz : theClazz.getImplementations()) {
                JavaClass cls = javaclasses.stream().filter(c -> {
                    return c.getClassName().equals(clazz.getClassName());
                }).findFirst().orElse(null);
                if (cls != null && (cls.getClassType().equals("INTERFACE") || cls.getClassType().contains("Abstract"))) {
                    ArrayList<JavaClass> clazzez = classesWhoImplementTheInterfaceOrExtendsThisClass(cls);
                    for (JavaClass impl : clazzez) {
                        getSubClasses(impl);
                    }
                    //if (tmpSubClazzz != null){
                    //    for (JavaClass subClass : tmpSubClazzz) {
                    //        getSubClasses(subClass);
                    //    }
                    //}
                } else if (cls != null) {
                    subClazzz.add(cls);
                }
            }
        }
        return new ArrayList<>();
    }

    private ArrayList<JavaClass> classesWhoImplementTheInterfaceOrExtendsThisClass(JavaClass cls) {
        ArrayList<JavaClass> all = new ArrayList<>();
        if (cls.getImplementations() != null) {
            for (JavaClass impl : cls.getImplementations()) {
                all.addAll(javaclasses.stream().filter(cl -> {
                    return cl.getClassName().equals(impl.getClassName());
                }).collect(Collectors.toList()));
            }
        }
        if (cls.getHeritances() != null) {
            for (JavaClass hers : cls.getHeritances()) {
                all.addAll(javaclasses.stream().filter(cl -> {
                    return cl.getClassName().equals(hers.getClassName());
                }).collect(Collectors.toList()));
            }
        }

        /*for (JavaClass c:javaclasses) {
            if (c.getImplementations() != null){
                all.addAll(c.getImplementations().stream().filter(cl->{ return cl.getClassName().equals(cls.getClassName());}).collect(Collectors.toList()));
            }
            if (c.getHeritances() != null){
                all.addAll(c.getHeritances().stream().filter(cl->{ return cl.getClassName().equals(cls.getClassName());}).collect(Collectors.toList()));
            }
        }*/
        return all;
    }


    public void newAlgo(JavaClass theClazz, Link link) {
        try {
            if (theClazz.getClassType().equals("INTERFACE") || theClazz.getClassName().contains("Abstract")) {
                ArrayList<JavaClass> clazzez = classesWhoImplementTheInterfaceOrExtendsThisClass(theClazz);
                for (JavaClass clazz : clazzez) {
                    if (!clazz.getClassType().equals("INTERFACE") && !clazz.getClassType().contains("Abstract")) {
                        //findPersistent and save
                        findPersistentForClass(clazz, link);
                        continue;
                    }
                    newAlgo(clazz, link);
                }
            } else {
                findPersistentForClass(theClazz, link);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void findPersistentForClass(JavaClass theClazz, Link link) {
        Persistent lclpers = persistences.stream().filter(per -> {
            return per.getName().equals(theClazz.getClassName());
        }).findFirst().orElse(null);
        if (lclpers != null) {
            virtualLinksPoints.put(lclpers.getId(), link.getId());
        }
    }


    ///vérification en utilisant les données
    //loop on all persistens => {perss}
    // loop on {perss}.links =>{link}
    // loop on all persistents {per}
    //loop on {per}.links =>{perlink}
    ///select * from {perss}.getName() =>formatted to table name
    // where exists(select * from {per} where {perlink}.name+_oid = {link}.name+_oid and oid <> oid      //pour éviter de selectionner le meme objet
    // if exists ==> add :
    // source = perss,
    // target = per ,
    // link_source = link
    // link_target = perlink

    public void dataModelTraining() {
        ArrayList<Persistent> persistents = persistences;
        for (Persistent persistent : persistents) {
            for (Link link : persistent.getLinks()) {
                for (Persistent per : persistents) {
                    for (Link perLink : per.getLinks()) {
                        if (per.getId() != persistent.getId() && link.getId() != perLink.getId()) {
                            execute(persistent, link, per, perLink);

                        }
                    }
                }
            }
        }
    }

    ///select * from {perss}.getName() =>formatted to table name
    // where exists(select * from {per} where {perlink}.name+_oid = {link}.name+_oid and oid <> oid      //pour éviter de selectionner le meme objet
    private boolean execute(Persistent persistent, Link link, Persistent per, Link perLink) {
        connect("ORA", false);
        try {
            PlasmaObjectTableName nameProcessor = new PlasmaObjectTableName();
            Statement statement = conn.createStatement();
            try {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM " + nameProcessor.getNameFor(persistent) + " WHERE " +
                        "EXISTS (SELECT * FROM " + nameProcessor.getNameFor(per) + " WHERE " + nameProcessor.getNameFor(perLink) + " = " + nameProcessor.getNameFor(link) +
                        " ) FETCH FIRST 1 ROWS ONLY");
                if (resultSet.isBeforeFirst()) {
                    virtualLinksPointsV2.add(String.valueOf(persistent.getName() + " has " + link.getName() + " references on " + per.getName() + " " + perLink.getName()));
                }
            } catch (Exception e) {
                //try to get link type from element type on the link
                String elementType = null;
                JavaClass javaClass = null;
                if (link.getElementType() != null) {
                    elementType = link.getElementType().substring(link.getElementType().lastIndexOf(".") + 1);
                    String finalElementType = elementType;
                    javaClass = javaclasses.stream().filter(jc -> jc.getClassName().equals(finalElementType)).findFirst().orElse(null);
                    if (javaClass != null) {
                        JavaField jf = javaClass.getJavaFields().stream().filter(javaField -> javaField.getName().equals(link.getName())).findFirst().orElse(null);
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + nameProcessor.getNameFor(persistent) + " WHERE " +
                                "EXISTS (SELECT * FROM " + nameProcessor.getNameFor(String.valueOf(elementType)) + " WHERE " + nameProcessor.getNameFor(perLink) + " = " + nameProcessor.getNameFor(link) +
                                " ) FETCH FIRST 1 ROWS ONLY");
                        if (resultSet.isBeforeFirst()) {
                            virtualLinksPointsV2.add(String.valueOf(persistent.getName() + " has " + link.getName() + " references on " + per.getName() + " " + perLink.getName()));
                        }
                    }
                }


            }

        } catch (SQLException throwables) {
            return true;
        }
        return false;
    }


    // same for field to field : example : fo.external_id = accImpl.reference
    //+ add isfield isLink to the table virtualLink


    public void lastAlgo() {
        assignPersistentsToJavaClasses();
        filterJavaClasses();
        for (Link link : links) {
            getElementTypeFromJavaClass(link);
        }
    }

    private void assignPersistentsToJavaClasses() {
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

    private void getElementTypeFromJavaClass(Link link) {
        for (JavaClass javaClass : parsedJavaclasses) {
            Persistent per = javaClass.getPersistent();
            if (per != null) {
                for (Link l : per.getLinks()) {
                    if (l == link) {
                        link.setElementType(getType(javaClass, per, link));
                    }
                }
            }
        }

    }

    private String getType(JavaClass javaClass, Persistent per, Link link) {
        JavaProjectBuilder javaProjectBuilder = javaClass.getBody();
        com.thoughtworks.qdox.model.JavaClass parsed = javaProjectBuilder.getClasses().iterator().next();
        String capitalizedName = link.getName().substring(0, 1).toUpperCase() + link.getName().substring(1);
        JavaMethod jm = parsed.getMethod("get" + capitalizedName, null, false);
        JavaMethodDelegate delegate = new JavaMethodDelegate(parsed, jm);
        return delegate.getReturns().getName();
    }


    private void filterJavaClasses() {
        javaclasses = (ArrayList<JavaClass>) javaclasses.stream().filter(javaClass -> javaClass.getPersistent() != null && javaClass.getClassType().equals("CLASS")).collect(Collectors.toList());
        parsedJavaclasses = (ArrayList<JavaClass>) parsedJavaclasses.stream().filter(javaClass -> javaClass.getPersistent() != null && javaClass.getClassType().equals("CLASS")).collect(Collectors.toList());
    }


    ////new Processor parser

    private void startGlobal() {

        connect("", false);

        System.out.println("ok");
        processorPersistence();
        initLinks();
        initPersistents();
        assignLinksToPersistents();
        try {
            JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
            javaProjectBuilder.addSourceTree(new File("C:\\sandboxes\\solife_6_3_x\\is\\modules\\cas"));
            javaProjectBuilder.addSourceTree(new File("C:\\sandboxes\\solife_6_3_x\\is\\modules\\pas"));
            //javaProjectBuilder.addSourceTree(new File("C:\\sandboxes\\solife_6_3_x\\is\\modules\\businessframework"));
            javaProjectBuilder.addSourceTree(new File("C:\\sandboxes\\solife_6_3_x\\is\\modules\\collection-disbursement"));
            javaProjectBuilder.addSourceTree(new File("C:\\sandboxes\\solife_6_3_x\\is\\modules\\pricing-finance"));
            javaProjectBuilder.addSourceTree(new File("C:\\sandboxes\\solife_6_3_x\\is\\modules\\reinsurance"));
            javaProjectBuilder.addSourceTree(new File("C:\\sandboxes\\solife_6_3_x\\is\\modules\\services"));
            javaProjectBuilder.addSourceTree(new File("C:\\sandboxes\\solife_6_3_x\\is\\modules\\thirdpartymanagement"));
            Collection<com.thoughtworks.qdox.model.JavaClass> filtered = filter(javaProjectBuilder);
            try {
                for (Persistent persistent : persistences) {
                    if (persistent.getLinks() != null) {
                        for (Link link : persistent.getLinks()) {
                            try {
                                com.thoughtworks.qdox.model.JavaClass clazz = getClassByName(persistent.getName(), filtered);
                                //getVariable
                                String type;
                                if (link.getInverseName() != null) {
                                    type = findClassForInverseName(clazz, filtered, link, persistent);//dbname
                                } else {
                                    type = getVariableReturnType(clazz, link);//dbname
                                }
                                System.out.println("return type ( " + type + " ) found for " + link.getName());
                                varsUp.add(link.getName() + " on " + persistent.getName() + ", return type = " + type);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }


        System.out.println("ok");
    }

    private String findClassForInverseName(com.thoughtworks.qdox.model.JavaClass clazz, Collection<com.thoughtworks.qdox.model.JavaClass> filtered, Link link, Persistent persistent) {
        for (Link lin : links) {
            if (link.getInverseName().equals(lin.getName())) {
                if (lin.getElementType() != null) {
                    String elementType = link.getElementType().substring(link.getElementType().lastIndexOf(".") + 1);
                    //if (elementType.equals(clazz.getName())) {
                    Persistent pers = findPersistentById(lin.getPersistentId());
                    com.thoughtworks.qdox.model.JavaClass elementTypeClass = getClassByName(pers.getName(), filtered);
                    return getVariableReturnType(elementTypeClass, lin);
                    //}
                } else if (isInPlural(lin.getName(), filtered)) {
                    String singular = lin.getName().substring(0, lin.getName().length() - 1);
                    //Persistent pers = findPersistentById(lin.getPersistentId());
                    com.thoughtworks.qdox.model.JavaClass elementTypeClass = getClassByName(singular, filtered);
                    return getVariableReturnType(elementTypeClass, lin);
                }
            }
        }
        return null;
    }

    private boolean isInPlural(String name, Collection<com.thoughtworks.qdox.model.JavaClass> classes) {
        if (!(name.lastIndexOf("s") == name.length())) {
            return false;
        }
        String singular = name.substring(0, name.length() - 1);
        return getClassByName(singular, classes) != null;

    }


    public Collection<com.thoughtworks.qdox.model.JavaClass> filter(JavaProjectBuilder javaProjectBuilder) {
        return javaProjectBuilder.getClasses().stream().filter(jc -> !jc.getSource().getURL().getPath().contains("src/test") && !jc.getSource().getURL().getPath().contains("target/")).collect(Collectors.toList());
    }

    public com.thoughtworks.qdox.model.JavaClass getClassByName(String name, Collection<com.thoughtworks.qdox.model.JavaClass> classes) {
        return classes.stream().filter(cls -> cls.getName().equals(name)).findFirst().orElse(null);
    }

    ArrayList<String> varsDown = new ArrayList<>();
    ArrayList<String> varsUp = new ArrayList<>();

    public String getVariableReturnType(com.thoughtworks.qdox.model.JavaClass javaClass, Link link) {
        String var = link.getName();
        String capitalizedName = var.substring(0, 1).toUpperCase() + var.substring(1);
        try {
            JavaMethod jm = javaClass.getMethod("get" + capitalizedName, null, false);
            DefaultJavaParameterizedType returns = (DefaultJavaParameterizedType) jm.getReturns();
            if (returns.getActualTypeArguments().isEmpty()) {
                return returns.getName();
            } else {
                return returns.getActualTypeArguments().get(0).getCanonicalName();
            }
        } catch (Exception exception) {
            System.out.println("cannot find method for " + var);
            varsDown.add(var + " on " + javaClass.getName());
        }
        return null;
    }

    public Persistent findPersistentByName(String name) {
        return persistences.stream().filter(per -> per.getName().equals(name)).findFirst().orElse(null);
    }

    public Persistent findPersistentById(long id) {
        return persistences.stream().filter(per -> per.getId() == id).findFirst().orElse(null);
    }

    private static String basePath = "C:/Sandboxes/solife_6_1_2_CLV23_FP";
    private static Report report = new Report();

    public void processorPersistence() {
        AbstractMapper abstractMapperPersistence = initAbstractMapper(ProcessingTypes.PERSISTENT, FileTypes.PERSISTENCE, basePath, report);

        try {
            p2(abstractMapperPersistence);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        //this.persistences = abstractMapperPersistence.getProcess().getPersistents();
    }

    private static AbstractMapper initAbstractMapper(String processingType, String fileType, String basePath, Report report) {
        AbstractProcess abstractProcess = new AbstractProcess(processingType, basePath, report);
        ProjectImpl project = abstractProcess.createProject(basePath);
        AbstractMapper abstractMapper = new AbstractMapper(project, fileType);
        abstractMapper.setProject(project);
        abstractMapper.setProcess(abstractProcess);
        return abstractMapper;
    }

    private void p2(AbstractMapper abstractMapperPersistence) throws Exception {

        ProjectImpl projectP = abstractMapperPersistence.getProject();
        //projectP.setProjectPersistenceFiles(abstractMapperPersistence.getProjectFiles());

        try {
            abstractMapperPersistence.getProcess().adaptProcess(abstractMapperPersistence.getProjectFiles(), ProcessingTypes.PERSISTENT);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void p3(AbstractMapper abstractMapperProperties) throws Exception {
        ProjectImpl projectP2 = abstractMapperProperties.getProject();
        //projectP2.setProjectPropertiesFiles(abstractMapperProperties.getProjectFiles());

        try {
            abstractMapperProperties.getProcess().adaptProcess(abstractMapperProperties.getProjectFiles(), ProcessingTypes.PROPERTY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    ////CID ANALYSIS

    private void doProcess() {
        //virtualLinkCreationProcess.process();
        //virtualLinkCreationProcess.dataModelTraining();
        try {
            connect("", false);
            processorPersistence();
            initLinks();
            initPersistents();
            assignLinksToPersistents();
            AbstractMapper abstractMapperProperties = initAbstractMapper(ProcessingTypes.PROPERTY, FileTypes.CIDREG, basePath, report);
            p3(abstractMapperProperties);
            mergeProperties(abstractMapperProperties.getProcess().getProperties());
            seedPropoerties();
            processAnalysis(abstractMapperProperties.getProcess().getProperties());
            System.out.println("test ok");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        System.out.println("test ok");
    }

    private HashMap<String, String> mergedProperties = new HashMap<>();
    private HashMap<String, String> linkToPersistent = new HashMap<>();

    private void mergeProperties(ArrayList<PropertiesFile> properties) {
        for (PropertiesFile props : properties) {
            Properties prp = props.getProperties();
            for (Map.Entry property : prp.entrySet()) {
                mergedProperties.put((String) property.getValue(), (String) property.getKey());
            }
        }
    }


    private void processAnalysis(ArrayList<PropertiesFile> properties) {
        connect("ORA", true);
        try {
            Statement statement = conn.createStatement();
            for (Persistent persistent : persistences) {
                if (persistent.isPersistent()){
                    for (Link link : persistent.getLinks()) {
                        if (link.getInverseName() != null){
                            //String linkName = linkName.replaceAll("([A-Z])", "_$1").concat("_OID").toUpperCase();
                            String linkNameCid;
                            if (link.getDbname() == null) {
                                linkNameCid = convertToUnderscoredName(link.getName()).concat("_CID").toUpperCase();
                            } else {
                                String name = "";
                                String[] splits = link.getDbname().split(",");
                                for (String split : splits) {
                                    if (split.contains("CID") || split.contains("cid")) {
                                        name = split;
                                    }
                                }
                                linkNameCid = name;
                            }
                            String table = "";
                            if (persistent.getTable() == null) {
                                table = convertToUnderscoredName(persistent.getName()).toUpperCase();
                            } else {
                                table = persistent.getTable();
                            }
                            try {
                                ResultSet resultSet = statement.executeQuery("SELECT DISTINCT " + linkNameCid + " FROM " + table + " WHERE 1=1 ");//flip oid and cid in some cases
                                while (resultSet.next()) {
                                    String cid = resultSet.getString(1);
                                    String type = mergedProperties.get(cid);
                                    linkToPersistent.put(link.getName(), type);
                                }
                                resultSet.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        linkToPersistent.values();
    }


    private void seedPropoerties() {
    }

    private String convertToUnderscoredName(String name) {
        List<String> splits = Arrays.asList(name.split(""));
        String result = "";
        int i = -1;
        for (String split : splits) {
            boolean isFirst = i == 0;
            if (isFirst) {
                result = result.concat(split.toLowerCase());
                i++;
                continue;
            }
            i++;
            boolean isLast = i==name.length()-1;
            char currentChar = split.charAt(0);
            char nextChar = isLast? 0:splits.get(i+1).charAt(0);
            char prevChar = i-1<0? 0:splits.get(i-1).charAt(0);
            int lastIndexUnderscore = result.lastIndexOf("_");
            if ((!Character.isUpperCase(prevChar) && Character.isUpperCase(currentChar) ) || (Character.isUpperCase(nextChar) && Character.isUpperCase(currentChar) && !(lastIndexUnderscore!=i-2))) {
            //if (Character.isUpperCase(split.charAt(0)) && !isLast && !Character.isUpperCase(splits.get(i+1).charAt(0))) {
                String mod = "_" + split.toLowerCase();
                result = result.concat(mod);
            } else {
                result = result.concat(split.toLowerCase());
            }
        }
        return result;
    }
}
