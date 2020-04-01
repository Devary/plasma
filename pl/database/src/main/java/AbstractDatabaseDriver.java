/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

import java.sql.*;

public class AbstractDatabaseDriver {

    private static String USER = "plasma";
    private static String PASSWORD = "plasma";
    private static String SID = "orcl";
    private static String TEST = "tst";


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

    public static void main(String[] args) {
        connect();
    }
}
