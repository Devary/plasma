package Engines.custom_engines;

import Engines.AbstractEngine;
import Engines.Engine;
import Engines.connections.MysqlConnection;
import hierarchy.persistence.types.SolifeQuery;

import java.sql.SQLException;
import java.sql.Statement;

public class SolifeQueryEngine extends Engine implements AbstractEngine {
    @Override
    public boolean create(Object object) throws SQLException {
        SolifeQuery solifeQuery = (SolifeQuery) object;
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO queries Values (null,");
        query.append("'"+solifeQuery.getName()+"'");
        query.append(",null");
        query.append(",null");
        query.append(",null)");

        Statement st = super.getConnection().createStatement();
        try {
            st.executeUpdate(query.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Object object) {
        return false;
    }
}
