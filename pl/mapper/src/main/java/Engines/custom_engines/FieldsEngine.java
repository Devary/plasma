package Engines.custom_engines;

import Engines.AbstractEngine;
import Engines.Engine;
import hierarchy.persistence.types.Link;

import java.sql.SQLException;
import java.sql.Statement;

public class FieldsEngine extends Engine implements AbstractEngine {
    @Override
    public boolean create(Object object) {
        Link link = (Link) object;
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO links Values (null,");
        query.append("null");
        query.append(",null");
        query.append(","+link.getReferenceIntegrityCheck());
        query.append(","+link.getInverseName()+");");
        Statement st = super.getStatement();
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
