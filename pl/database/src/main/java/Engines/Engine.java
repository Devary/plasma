package Engines;

import Engines.connections.MysqlConnection;
import Engines.connections.OracleConnection;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

public class Engine {
    private static Statement statement;
    private String connectionType = null;
    public Engine() {
            getConnectionTypeParam();
            if (connectionType != null)
            {
                if (connectionType.equals("MYSQL") ){
                    MysqlConnection.getInstance().getConnection();
                    statement = MysqlConnection.getInstance().getStatement();
                }
                if (connectionType.equals("ORACLE")){
                    OracleConnection.getInstance().getConnection();
                    statement = OracleConnection.getInstance().getStatement();
                }
            }
        
    }

    public static void initOracleSequences() {
        StringBuilder query = new StringBuilder();
        Properties props = null;
        try {
            props = PropertiesExtractor.getAllProperties("tables");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(props != null || !props.isEmpty()){
            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                Object p = entry.getKey();
                Object o2 = entry.getValue();
                query.append("SELECT COUNT(*) AS rowcount FROM USER_SEQUENCES WHERE SEQUENCE_NAME =");
                query.append("'"+o2+"'");
                try {
                    ResultSet rs = statement.executeQuery(query.toString());
                    while (rs.next()) {
                        int nbRows = rs.getInt("rowcount");
                        if (nbRows == 0) {
                            StringBuilder createSequence = new StringBuilder();
                            createSequence.append("CREATE TRIGGER ");
                            createSequence.append(o2 + "_id ");
                            createSequence.append("BEFORE INSERT ON " + o2);
                            createSequence.append(" FOR EACH ROW ");
                            createSequence.append("BEGIN ");
                            createSequence.append("SELECT " + o2 + "_id_seq.nextval INTO :new.ID FROM dual; ");
                            createSequence.append("END;");
                            statement.executeQuery(createSequence.toString());
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void getConnectionTypeParam() {
        try {
            connectionType = PropertiesExtractor.getPropValue("params","connection.type");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Statement getStatement() {
        return statement;
    }
}
