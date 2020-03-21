package Hierarchy.persistence.types;

public class Field implements IField {

    private String name;
    private String dbtype;
    private String dbname;
    private String dbsize;
    private String defaultValue;
    private boolean allowsNull= false;


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public boolean isAllowsNull() {
        return allowsNull;
    }

    public void setAllowsNull(boolean allowsNull) {
        this.allowsNull = allowsNull;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDbsize() {
        return dbsize;
    }

    public void setDbsize(String dbsize) {
        this.dbsize = dbsize;
    }
}
