/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package hierarchy.persistence.types;

public class Code implements IField {


    private String name;
    private String DbName;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setDbname(String dbName) {
        this.DbName = dbName;
    }

    public String getDbName() {
        return DbName;
    }

}
