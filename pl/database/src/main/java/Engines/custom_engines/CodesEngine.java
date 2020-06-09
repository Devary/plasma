package Engines.custom_engines;

import Engines.AbstractEngine;
import Engines.Engine;
import hierarchy.persistence.types.Code;
import hierarchy.persistence.types.Link;

import java.sql.SQLException;
import java.sql.Statement;

public class CodesEngine extends Engine implements AbstractEngine {
    @Override
    public boolean create(Object object) {
        //TODO : links are empty , must be checked
        Code code = (Code) object;
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO codes Values (null");
        if (code.getName() == null){
            query.append(",null");
        }else {
            query.append(",'"+code.getName()+"'");
        }
        query.append(",null");
        if (code.getDbName() == null){
            query.append(",null");
        }else {
            query.append(",'"+code.getDbName()+"'");
        }
        query.append(")");
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
