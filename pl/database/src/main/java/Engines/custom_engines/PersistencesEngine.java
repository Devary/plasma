package Engines.custom_engines;

import Engines.AbstractEngine;
import Engines.Engine;
import hierarchy.persistence.Persistent;

public class PersistencesEngine extends Engine implements AbstractEngine {

    @Override
    public boolean create(Object object) {
        Persistent persistent = (Persistent) object;
        StringBuilder query = new StringBuilder();
        Fluent fluent = new Fluent();
        query.append("INSERT INTO persistences Values (null,");
        query.append("'" + persistent.getClassName() + "',");
        query.append("null,");
        if (persistent.getMappingType() != null) {
            query.append("'" + persistent.getMappingType() + "'");
        } else {
            query.append(",null");
        }
        query.append(",1");
        if (persistent.getTableName() != null) {
            query.append(",'" + persistent.getTableName() + "'");
        } else {
            query.append(",null");
        }
        if (persistent.getShortTableName() != null) {
            query.append(",'" + persistent.getShortTableName() + "'");
        } else {
            query.append(",null");
        }
        query.append(")");
        fluent.executeUpdate(query);
        return false;
    }

    @Override
    public boolean delete(Object object) {
        return false;
    }
}
