package Engines.connections;

import org.apache.log4j.Logger;
import services.processing.VirtualLinkCreationProcess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class OracleConnection implements AbstractConnection{
    private static OracleConnection instance;
    private Connection connection;
    private Statement statement;
    private static Logger logger = Logger.getLogger(OracleConnection.class);

    private OracleConnection() {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");

            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:orcl","plasma","plasma");
            statement = connection.createStatement();

        }catch(Exception e){ logger.warn(e);}
    }
    public static OracleConnection getInstance(){
        if(instance == null){
            instance = new OracleConnection();
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
