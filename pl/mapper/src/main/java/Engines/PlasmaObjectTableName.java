package Engines;

import hierarchy.Classes.JavaClass;
import hierarchy.persistence.Persistent;
import hierarchy.persistence.types.Code;
import hierarchy.persistence.types.Field;
import hierarchy.persistence.types.Link;
import hierarchy.persistence.types.SolifeQuery;

import java.util.Arrays;
import java.util.List;

public class PlasmaObjectTableName {
    public String getTableNameFor(Object obj) {
        if (obj.equals(SolifeQuery.class)){
            return "queries";
        }else if (obj.equals(Code.class)){
            return "codes";
        }else if (obj.equals(Field.class)){
            return "fields";
        }else if (obj.equals(Link.class)){
            return "links";
        }else if (obj.equals(Persistent.class)){
            return "persistences";
        }else if (obj.equals(JavaClass.class)){
            return "classes";
        }
        return null;
    }

    public String getTableNameForObject(Object obj){
        if (obj.equals(SolifeQuery.class)){
            return "queries";
        }
        return null;
    }

    public String getNameFor(Object obj) {
        if (obj instanceof Persistent){
            Persistent persistent = (Persistent) obj;
            if (persistent.getTable() != null){
                return persistent.getTable();
            }
            return convertToUnderscoredName(persistent.getName());
        }
        if (obj instanceof Link){
            Link link = (Link) obj;
            return convertToUnderscoredName(link.getName())+"_OID";
        }

        if (obj instanceof String){
            return convertToUnderscoredName((String) obj)+"_OID";
        }
        return null;
    }

    private String convertToUnderscoredName(String name) {
        List<String> splits = Arrays.asList(name.split(""));
        String result="";
        int i=0;
        for (String split:splits) {
            boolean isFirst = i==0;
            if (isFirst){
                result = result.concat(split.toLowerCase());
                i++;
                continue;
            }
            if (Character.isUpperCase(split.charAt(0))){
                String mod = "_"+split.toLowerCase();
                result = result.concat(mod);
            }else {
                result = result.concat(split.toLowerCase());
            }
        }
        return result;
    }
}
