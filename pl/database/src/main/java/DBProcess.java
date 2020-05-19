import Engines.PlasmaObjectTableName;
import Engines.connections.MysqlConnection;
import hierarchy.Classes.JavaClass;
import hierarchy.persistence.Persistent;
import hierarchy.persistence.types.Code;
import hierarchy.persistence.types.Field;
import hierarchy.persistence.types.Link;
import hierarchy.persistence.types.SolifeQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DBProcess {

    private ArrayList<JavaClass> javaClasses;

    public static void init(ArrayList<JavaClass> javaClasses) {
        javaClasses = javaClasses;
        /*if (!javaClasses.isEmpty()){
            filterJavaClassesByPersistenceExistence();
        }*/
        globalProcess(javaClasses);
    }
    public ArrayList<JavaClass> getJavaClasses() {
        return javaClasses;
    }

    private void filterJavaClassesByPersistenceExistence(){
        javaClasses = getJavaClasses().stream().filter(jc -> jc.getPersistent()!= null).collect(Collectors.toCollection(ArrayList::new));
    }

    public static void globalProcess(ArrayList<JavaClass> javaClasses){
        preProcess(javaClasses);
        creationProcess(javaClasses);
        postProcess(javaClasses);
    }

    private static void creationProcess(ArrayList<JavaClass> javaClasses) {
        ModelEngine modelEngine = new ModelEngine();

        for (JavaClass javaClass:javaClasses){
            ///starting with java class
            modelEngine.store(javaClass);
            if (javaClass.getPersistent() != null){
                Persistent p  = javaClass.getPersistent();
                modelEngine.store(p);
                subProcess(p.getFields(),modelEngine);
                subProcess(p.getCodes(),modelEngine);
                subProcess(p.getLinks(),modelEngine);
                subProcess(p.getQueries(),modelEngine);
            }
        }
    }

    private static void subProcess(Object objects, ModelEngine modelEngine) {
        ArrayList<Object> myobjs= (ArrayList<Object>) objects;
        if (!myobjs.isEmpty()){
            myobjs.forEach(modelEngine::store);
        }
    }

    private static void postProcess(ArrayList<JavaClass> javaClasses) {
        //update foreing keys
        // waiting for future decisions
    }

    private static void preProcess(ArrayList<JavaClass> javaClasses) {
        // this method will call all the performance optimization processes
        // first step cleaning the database
        truncateAllTables();
    }
    public static void truncateAllTables()
    {

        // here we will truncate all the plasma tables
        StringBuilder q = new StringBuilder().append("DELETE FROM ");
        ArrayList<Object> objects = getAllObjectTypes();
        Connection c = MysqlConnection.getInstance().getConnection();
        System.out.println("############## TRUNCATING TABLES ##############");
        System.out.println("_______________________________________________");
        for (Object obj:objects){
            String tableName = PlasmaObjectTableName.getTableNameFor(obj);
            String query = q.toString()+tableName+";";
            int numberOfRows = 0;
            try {
                Statement statement = c.createStatement();
                ResultSet resultSet = statement.executeQuery("select count(*) from "+tableName);
                while (resultSet.next()) {
                    numberOfRows = resultSet.getInt(1);
                }
                statement.executeUpdate(query);
                System.out.println("** (!) **  Table "+tableName+" : "+numberOfRows+" are deleted !");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("_______________________________________________");
        System.out.println("############## Tables are clear ! ##############");


    }
    private static ArrayList<Object> getAllObjectTypes() {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(SolifeQuery.class);
        objects.add(Link.class);
        objects.add(JavaClass.class);
        objects.add(Code.class);
        objects.add(Field.class);
        objects.add(Persistent.class);
        return objects;
    }

}
