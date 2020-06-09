package Engines.custom_engines;

import Engines.AbstractEngine;
import Engines.Engine;
import hierarchy.Classes.JavaClass;
import hierarchy.persistence.Persistent;
import hierarchy.persistence.types.Code;

import java.sql.SQLException;
import java.sql.Statement;

public class ClassEngine extends Engine implements AbstractEngine {
    @Override
    public boolean create(Object object) {
        //TODO : links are empty , must be checked
        JavaClass javaClass = (JavaClass) object;
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO classes Values ("+DatabaseGlobalAttributes.NULL);
        if (javaClass.getClassName() == null){
            query.append(",null");
        }else {
            query.append(",'"+javaClass.getClassName()+"'");
        }
        if (javaClass.getPersistent() !=null){
            Fluent fluent = new Fluent();
            Integer persistentId = fluent.find(DatabaseGlobalAttributes.PERSISTENCE_TABLE_NAME,javaClass.getPersistent());
            query.append(","+persistentId);
        }else {
            query.append(","+DatabaseGlobalAttributes.NULL);
        }
        if (javaClass.getHeritances() !=null && !javaClass.getHeritances().isEmpty()){
            Fluent fluent = new Fluent();
            javaClass.getHeritances().forEach(h -> {

            });
            Integer classId = fluent.find(DatabaseGlobalAttributes.CLASS_TABLE_NAME,javaClass.getPersistent());
            query.append(","+classId);
        }else {
            query.append(",null");
        }
        //query.append(","+DatabaseGlobalAttributes.NULL);
        query.append(","+DatabaseGlobalAttributes.NULL);
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
