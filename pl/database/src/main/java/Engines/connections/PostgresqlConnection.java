package Engines.connections;

import Engines.DBConstants;
import Engines.PropertiesExtractor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class PostgresqlConnection implements AbstractConnection, DBConstants{
    private static PostgresqlConnection instance;
    private Connection connection;
    private Statement statement;
    private final String connectionName = "postgre";
    public PostgresqlConnection() {
        try{
            String user = PropertiesExtractor.getPropValue(connectionName, DATABASE_USER);
            String password = PropertiesExtractor.getPropValue(connectionName, DATABASE_PASSWORD);
            String ssl = PropertiesExtractor.getPropValue(connectionName, DATABASE_SSL);
            String url = "jdbc:postgresql://localhost:5432/postgres";
            Properties props = new Properties();
            props.setProperty(DATABASE_USER,user);
            props.setProperty(DATABASE_PASSWORD,password);
            props.setProperty(DATABASE_SSL,ssl);
            connection =  DriverManager.getConnection(url,props);

        }catch(Exception e){ System.out.println(e);}
    }
    public static PostgresqlConnection getInstance(){
        if(instance == null){
            instance = new PostgresqlConnection();
        }
        return instance;
    }
    @Override
    public Connection getConnection() {
        if (connection != null){
            return connection;
        }
        return null;
    }
    @Override
    public void closeConnection() throws SQLException {
        connection.close();
    }
    @Override
    public Statement getStatement() {
        return statement;
    }
}
