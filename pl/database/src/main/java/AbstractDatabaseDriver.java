/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

import Engines.Engine;
import Engines.PropertiesExtractor;
import Engines.connections.MysqlConnection;
import Engines.connections.PostgresqlConnection;
import hierarchy.Classes.JavaClass;
import hierarchy.persistence.Persistent;
import hierarchy.persistence.types.SolifeQuery;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class AbstractDatabaseDriver {

    private static String USER = "plasma";
    private static String PASSWORD = "plasma";
    private static String SID = "orcl";
    private static String TEST = "tst";
    //public static void connectSQL(){
    //    try{
    //        MysqlConnection.getInstance().getConnection();
    //        Statement statement = MysqlConnection.getInstance().getStatement();
    //        ResultSet rs=statement.executeQuery("select * from queries");
    //        while(rs.next())
    //            System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
    //    }catch(Exception e){ System.out.println(e);}
    //}

    public static void createQueries() throws SQLException {
        SolifeQuery sq =  SolifeQuery.newSolifeQuery().name("ok").className("test").build();
        ModelEngine me = new ModelEngine();
        me.store(sq);
    }

    public static void main(String[] args) {
        //createQueries();
        try {
            if (PropertiesExtractor.getConnectionType().equals("postgre")) {
                PostgresqlConnection pgconnection = new PostgresqlConnection();
                pgconnection.getConnection().getClientInfo();
            }
            //connect(true);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        //connectSQL();
        //try {
        //    if (PropertiesExtractor.getPropValue("params","connection.type").equals("ORACLE")){
        //        Engine.initOracleSequences();
        //    }
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
//
        //MainProcess mainProcess = new MainProcess();
        //ArrayList<Persistent> pers = mainProcess.getPeristents();
        //ArrayList<JavaClass> javaClasses;
//
        ///// depracted
        //javaClasses = mainProcess.getJavaClasses();
        //DBProcess.init(javaClasses);

        //public static void main(String[] args) {
        //    //createQueries();
        //    connect(true);
        //    //connectSQL();
        //    try {
        //        if (PropertiesExtractor.getPropValue("params","connection.type").equals("ORACLE")){
        //            Engine.initOracleSequences();
        //        }
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //    }
        try {
            createQueries();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //MainProcess mainProcess = new MainProcess();
        //ArrayList<Persistent> pers = mainProcess.getPeristents();
        //ArrayList<JavaClass> javaClasses;
        ///// depracted
        //javaClasses = mainProcess.getJavaClasses();
        //DBProcess.init(javaClasses);
    }
}
