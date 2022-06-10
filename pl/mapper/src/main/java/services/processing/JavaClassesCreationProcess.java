/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import Exceptions.ClassNameNotFoundException;
import hierarchy.Classes.JavaClass;
import files.IAbstractFile;
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
                System.out.println(e.getMessage());
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
        System.out.println("Processing Object creation");
        int max = -500;
        int i = 0;
        ArrayList<JavaClass> javaClasses = new ArrayList<>();
        for (ProjectFile projectFile : projectJavaFiles) {
            i++;
            JavaClassesParsingService ps = null;
            try {
                ps = new JavaClassesParsingService(projectFile, report);
            } catch (ClassNameNotFoundException e) {
                e.printStackTrace();
            }
            JavaClass javaClass = ps.getJavaClass();
            if (javaClass.getClassName() != null)
                javaClasses.add(javaClass);
            System.out.println(javaClass.getClassName() + " built successfully !");
            //if (javaClass.getJavaFields().size()>0)
            //break;
            if (i == max){
                break;
            }
        }
        System.out.println("FINISHED :: " + javaClasses.size() + " Objects created !");
        this.javaClasses = javaClasses;
        connect();
        seedToDatabase();
        return javaClasses;
    }

    private void seedToDatabase() {
        for (JavaClass javaClass : this.javaClasses) {
            persist(javaClass);
        }
    }

    private void persist(JavaClass javaClass) {
        StringBuilder insertion = new StringBuilder("INSERT INTO public.java_class " +
                "(\"className\", \"classType\", implements, inherit, created_at, updated_at, persistent_id)" +
                " VALUES ( ?, ?, ?, ?, ?, ?, ?)");
        try {
            PreparedStatement statement = conn.prepareStatement(insertion.toString());
            statement.setString(1,javaClass.getClassName());
            statement.setString(2,javaClass.getClassType().toUpperCase());
            StringBuilder subs = new StringBuilder();
            if (javaClass.getImplementations() != null && !javaClass.getImplementations().isEmpty()){
                for (JavaClass impl:javaClass.getImplementations()){
                    subs.append(impl.getClassName());
                    subs.append(',');
                }
                statement.setString(3,subs.toString());
            }else{
                statement.setString(3,null);

            }
            if (javaClass.getHeritances() != null && !javaClass.getHeritances().isEmpty()){
                for (JavaClass her:javaClass.getHeritances()){
                    subs.append(her.getClassName());
                    subs.append(',');
                }
                statement.setString(4,subs.toString());
            }else{
                statement.setString(4,null);

            }

            java.util.Date date = new Date();
            Timestamp timestamp2 = new Timestamp(date.getTime());
            statement.setTimestamp(5,timestamp2);
            statement.setTimestamp(6,null);
            statement.setTimestamp(7,null);
            int update = statement.executeUpdate();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
