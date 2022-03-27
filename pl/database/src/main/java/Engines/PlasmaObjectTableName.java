package Engines;

import hierarchy.Classes.JavaClass;
import hierarchy.persistence.Persistent;
import hierarchy.persistence.types.Code;
import hierarchy.persistence.types.Field;
import hierarchy.persistence.types.Link;
import hierarchy.persistence.types.SolifeQuery;

public class PlasmaObjectTableName {
    public static String getTableNameFor(Object obj) {
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
}
