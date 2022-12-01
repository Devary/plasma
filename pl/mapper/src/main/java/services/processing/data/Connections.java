package services.processing.data;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connections {
    private static Connection conn = null;
    private static final String url = "jdbc:postgresql://localhost:5432/test";
    private static final String user = "postgres";
    private static final String password = "Fakher15";
    private static Logger logger = Logger.getLogger(Connections.class);

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public static Connection connect(String driver, boolean reset) {
        if (conn == null || reset) {
            try {
                conn = null;
                if (driver.equals("ORA")) {
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@10.6.140.67:1521:orcl", "CLV61PERF22", "CLV61PERF22");
                } else {
                    conn = DriverManager.getConnection(url, user, password);
                }
            } catch (SQLException e) {
                logger.warn(e.getMessage());
            }
        }
        return conn;

    }

    public Connection getConn() {
        return conn;
    }
}
