package Engines.connections;

import java.sql.*;

public class MysqlConnection {
    private static MysqlConnection instance;
    private Connection connection;
    private Statement statement;
    private MysqlConnection() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/plasma","root","");
            statement = connection.createStatement();
        }catch(Exception e){ System.out.println(e);}
    }

    public static MysqlConnection getInstance(){
        if(instance == null){
            instance = new MysqlConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection != null){
            return connection;
        }
        return null;
    }
    public void closeConnection() throws SQLException {
       connection.close();
    }

    public Statement getStatement() {
        return statement;
    }
}
