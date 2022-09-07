/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import Exceptions.ClassNameNotFoundException;
import hierarchy.Classes.JavaClass;
import files.IAbstractFile;
import hierarchy.Classes.types.Function;
import hierarchy.Classes.types.JavaField;
import org.apache.log4j.Logger;
import projects.ProjectFile;
import projects.ProjectImpl;
import services.parsing.JavaClassesParsingService;
import services.reporting.Report;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class JavaClassesCreationProcess implements IAbstractProcess {


    private Collection<JavaClass> javaClasses;

    public Collection<JavaClass> getJavaClasses() {
        return javaClasses;
    }
    private static Logger logger = Logger.getLogger(JavaClassesCreationProcess.class);

    private int id = -1;
    private Connection conn = null;

    private final String url = "jdbc:postgresql://localhost:5432/test";
    private final String user = "postgres";
    private final String password = "admin";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                logger.warn(e.getMessage());
            }
        }
        return conn;

    }

    private void setJavaClasses(Collection<JavaClass> javaClasses) {
        this.javaClasses = javaClasses;
    }

    @Override
    public ProjectImpl createProject(String basePath) {
        return null;
    }

    @Override
    public IAbstractFile createAbstractFile() {
        return null;
    }

    @Override
    public ArrayList<JavaClass> createObjectFiles(ArrayList<ProjectFile> projectJavaFiles, Report report) {
        logger.warn("Processing Object creation");
        int max = 2000;
        int i = 0;
        connect();
        ArrayList<JavaClass> javaClasses = new ArrayList<>();
        JavaClass javaClass;
        for (ProjectFile projectFile : projectJavaFiles) {
            i++;
            JavaClassesParsingService ps = null;
            try {
                ps = new JavaClassesParsingService(projectFile, report);
            } catch (ClassNameNotFoundException e) {
                e.printStackTrace();
            }
            javaClass = ps.getJavaClass();
            if (javaClass.getClassName() != null) {
                seedToDatabase(javaClass);
                //javaClasses.add(javaClass);
            }
            //logger.warn(javaClass.getClassName() + " built successfully !");
            //if (javaClass.getJavaFields().size()>0)
            //break;
            //if (i == max){
            //    logger.warn(i);
            //    break;
            //}
        }
        logger.warn("FINISHED :: " + javaClasses.size() + " Objects created !");
        this.javaClasses = javaClasses;
        //createPivotRelations(javaClasses);
        return javaClasses;
    }

    private void createPivotRelations(ArrayList<JavaClass> javaClasses) {
        for (JavaClass javaClass : javaClasses) {
            try {
                if (javaClass.getImplementations() != null) {
                    for (JavaClass impl : javaClass.getImplementations()) {
                        generatePivotBetween(javaClass, impl, "impl");
                    }
                    logger.warn(javaClass.getClassName() +": Impl Pivots are generated");
                }
                if (javaClass.getHeritances() != null) {
                    for (JavaClass impl : javaClass.getHeritances()) {
                        generatePivotBetween(javaClass, impl, null);
                    }
                    logger.warn(javaClass.getClassName() +": Superclass Pivot is generated");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void generatePivotBetween(JavaClass javaClass, JavaClass target, String pivotType) {
        int sourceId = javaClass.getId();
        int targetId = getTargetId(target.getClassName());
        if (targetId != -1) {
            persistPivot(sourceId, targetId, pivotType);
        }

    }

    private int getTargetId(String className) {
        StringBuilder insertion = new StringBuilder("SELECT ID FROM JAVA_CLASS WHERE \"className\" = '" + className + "' and \"classType\" = 'CLASS'");
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(insertion.toString());
            if (rs.next()) {
                return rs.getInt(1);
            }
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    private void persistPivot(int sourceId, int targetId, String pivotType) {
        String tableName = "public.class_inherit_pivot";
        StringBuilder insertion = new StringBuilder("INSERT INTO " + tableName + " (class_id, created_at, inherit_id) VALUES (?, ?, ?)");
        if (pivotType != null && pivotType.equals("impl")) {
            tableName = "public.class_implements_pivot";
            insertion = new StringBuilder("INSERT INTO " + tableName + " (class_id, created_at, implement_id) VALUES (?, ?, ?)");
        }
        try {
            java.util.Date date = new Date();

            Timestamp timestamp2 = new Timestamp(date.getTime());
            PreparedStatement statement = conn.prepareStatement(insertion.toString());
            statement.setInt(1, sourceId);
            statement.setTimestamp(2, timestamp2);
            statement.setInt(3, targetId);

            int update = statement.executeUpdate();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void seedToDatabase(JavaClass javaClass) {
        //for (JavaClass javaClass : this.javaClasses) {
            persist(javaClass);
            persistFields(javaClass);
            persistMethods(javaClass);
        //}
    }
    private void persistMethods(JavaClass javaClass) {
        connect();
        try{
            if (javaClass.getJavaFields() != null){
                for (Function jf:javaClass.getFunctions()){
                    try {
                        PreparedStatement statement = conn.prepareStatement("INSERT INTO public.java_method (name, result_type, created_at,class_id ,is_void) VALUES ( ?, ?, ?, ?, ?)");
                        statement.setString(1, jf.getName());
                        statement.setString(2, jf.getResultType());

                        java.util.Date date = new Date();
                        Timestamp timestamp2 = new Timestamp(date.getTime());
                        statement.setTimestamp(3, timestamp2);
                        statement.setInt(4, javaClass.getId());
                        statement.setBoolean(5, jf.isVoid());

                        int update = statement.executeUpdate();
                        statement.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void persistFields(JavaClass javaClass) {
        connect();
        try{
            if (javaClass.getJavaFields() != null){
                for (JavaField jf:javaClass.getJavaFields()){
                    try {
                        PreparedStatement statement = conn.prepareStatement("INSERT INTO public.java_field (name, type, created_at, class_id,collection_type,is_collection) VALUES ( ?, ?, ?, ?, ?,?)");
                        statement.setString(1, jf.getName());
                        statement.setString(2, jf.getType());

                        java.util.Date date = new Date();
                        Timestamp timestamp2 = new Timestamp(date.getTime());
                        statement.setTimestamp(3, timestamp2);
                        statement.setInt(4, javaClass.getId());
                        statement.setString(5, jf.getCollectionType());
                        statement.setBoolean(6, jf.isCollection());

                        int update = statement.executeUpdate();
                        statement.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void persist(JavaClass javaClass) {
        StringBuilder insertion = new StringBuilder("INSERT INTO public.java_class " +
                "(\"className\", \"classType\", implements, inherit, created_at, updated_at, persistent_id,full_path)" +
                " VALUES ( ?, ?, ?, ?, ?, ?, ?,?)");
        if (id != -1) {
            id = getMaxId("java_class");
        }
        try {
            PreparedStatement statement = conn.prepareStatement(insertion.toString());
            statement.setString(1, javaClass.getClassName());
            statement.setString(2, javaClass.getClassType().toUpperCase());
            StringBuilder subs = new StringBuilder();
            if (javaClass.getImplementations() != null && !javaClass.getImplementations().isEmpty()) {
                for (JavaClass impl : javaClass.getImplementations()) {
                    subs.append(impl.getClassName());
                    subs.append(',');
                }
                statement.setString(3, subs.toString());
            } else {
                statement.setString(3, null);

            }
            if (javaClass.getHeritances() != null && !javaClass.getHeritances().isEmpty()) {
                for (JavaClass her : javaClass.getHeritances()) {
                    subs.append(her.getClassName());
                }
                statement.setString(4, subs.toString());
            } else {
                statement.setString(4, null);

            }

            Statement statement1 = conn.createStatement();
            ResultSet rs = statement1.executeQuery("SELECT * FROM PERSISTENT WHERE NAME = '"+javaClass.getClassName()+"'");
            int idPers = -1;
            while (rs.next()){
                idPers = rs.getInt("id");
            }
            java.util.Date date = new Date();
            Timestamp timestamp2 = new Timestamp(date.getTime());
            statement.setTimestamp(5, timestamp2);
            statement.setTimestamp(6, null);
            if (idPers==-1){
                statement.setTimestamp(7, null);
            }else{
                statement.setInt(7, idPers);
            }
            statement.setString(8,javaClass.getPath());
            int update = statement.executeUpdate();
            if (update == 1) {
                if (id == -1) {
                    id = getMaxId("java_class");
                }
                javaClass.setId(id);
            }
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private int getMaxId(String tableName) {
        StringBuilder insertion = new StringBuilder("SELECT max(id) from " + tableName);
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(insertion.toString());
            if (rs != null && rs.next()) {
                return rs.getInt(1);
            }
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }
}
