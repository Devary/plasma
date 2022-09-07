package Engines.connections;

import org.apache.log4j.Logger;
import services.processing.VirtualLinkCreationProcess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlConnection implements AbstractConnection{
    private static MysqlConnection instance;
    private Connection connection;
    private Statement statement;
    private static Logger logger = Logger.getLogger(MysqlConnection.class);

    private MysqlConnection() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "","root","");
            statement = connection.createStatement();
        }catch(Exception e){ logger.warn(e);}
    }
    public static MysqlConnection getInstance(){
        if(instance == null){
            instance = new MysqlConnection();
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
