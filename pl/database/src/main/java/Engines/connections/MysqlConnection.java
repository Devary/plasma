package Engines.connections;

import java.sql.*;

public class MysqlConnection implements AbstractConnection {
    private static MysqlConnection instance;
    private Connection connection;
    private Statement statement;

    private MysqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "ssss", "root", "");
            statement = connection.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static MysqlConnection getInstance() {
        if (instance == null) {
            instance = new MysqlConnection();
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        if (connection != null) {
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
