package Engines.custom_engines;

import Engines.Engine;
import hierarchy.Classes.JavaClass;
import hierarchy.persistence.Persistent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Fluent extends Engine {
    public Integer find(String tableName ,Object object) {
        String attr=null;
        String colName=null;
        if (object instanceof Persistent) {
            attr = ((Persistent) object).getClassName();
            colName = "name";
        }
        if (object instanceof JavaClass) {
            attr = ((JavaClass) object).getClassName();
            colName = "name";
        }
        return this.find(tableName,colName,attr);
    }
    private Integer find(String tableName, String colName,String attr) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * from "+tableName+" t where t."+colName+" = '"+attr+"'");
        ResultSet rs = this.executeQuery(query);
        try {
            while (rs.next()){
                int id = rs.getInt("id");
                return id;
                //find("id",String.valueOf(id));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public ResultSet executeQuery(StringBuilder query) {
        Statement st = super.getStatement();
        try {
            return st.executeQuery(query.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void executeUpdate(StringBuilder query) {
        Statement st = super.getStatement();
        try {
            st.executeUpdate(query.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
