/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package hierarchy.persistence.types;

public class Field implements IField {

    private String name;
    private String dbtype;
    private String dbname;
    private String dbsize;
    private String defaultValue;
    private boolean allowNulls = false;


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

    public boolean isAllowNulls() {
        return allowNulls;
    }

    public void setAllowNulls(boolean allowNulls) {
        this.allowNulls = allowNulls;
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
