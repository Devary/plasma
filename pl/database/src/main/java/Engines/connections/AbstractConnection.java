package Engines.connections;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface AbstractConnection {
    public Statement getStatement();
    public void closeConnection()throws SQLException;
    public Connection getConnection();
}
