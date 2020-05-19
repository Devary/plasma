/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

import Engines.connections.MysqlConnection;
import hierarchy.Classes.JavaClass;
import hierarchy.persistence.types.SolifeQuery;

import java.sql.*;
import java.util.ArrayList;

public class AbstractDatabaseDriver {

    private static String USER = "plasma";
    private static String PASSWORD = "plasma";
    private static String SID = "orcl";
    private static String TEST = "tst";
    public static void connectSQL(){
        try{
            MysqlConnection.getInstance().getConnection();
            Statement statement = MysqlConnection.getInstance().getStatement();
            ResultSet rs=statement.executeQuery("select * from queries");
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
        }catch(Exception e){ System.out.println(e);}
    }

    public static void connect()
    {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection con=DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:orcl","plasma","plasma");

            Statement stmt=con.createStatement();

            ResultSet rs=stmt.executeQuery("select * from emp");
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));

            con.close();

        }catch(Exception e){ System.out.println(e);}

    }
    public static void createQueries(){
        SolifeQuery sq =  SolifeQuery.newSolifeQuery().name("ok").className("test").build();
        ModelEngine me = new ModelEngine();
        me.store(sq);

    }

    public static void main(String[] args) {
        createQueries();
        connect();
        connectSQL();
        MainProcess mainProcess = new MainProcess();
        ArrayList<JavaClass> javaClasses = mainProcess.getJavaClasses();
        DBProcess.init(javaClasses);
    }
}
