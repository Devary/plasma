package Hierarchy.persistence;

import files.AbstractFile;
import Hierarchy.persistence.types.Field;
import Hierarchy.persistence.types.Links;
import Hierarchy.persistence.types.SolifeQuery;

import java.util.ArrayList;

public class Persistent implements IPersistent {

    private String className;
    private String name;
    private String mappingType;
    private boolean isPersistent;
    private String tableName;
    private String shortTableName;
    private ArrayList<Field> fields;
    private ArrayList<Links> links;
    private ArrayList<SolifeQuery> queries;

    private Persistent(Builder builder) {
        this.className = builder.className;
        this.name = builder.name;
        this.mappingType = builder.mappingType;
        this.isPersistent = builder.isPersistent;
        this.tableName = builder.tableName;
        this.shortTableName = builder.shortTableName;
        this.fields = builder.fields;
        this.links = builder.links;
        this.queries = builder.queries;
    }

    public static Builder newPersistent() {
        return new Builder();
    }

    
    public String getName() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getExtension() {
        return null;
    }

    
    public AbstractFile refresh() {
        return null;
    }

    
    public ArrayList<AbstractFile> getChildren() {
        return new ArrayList<AbstractFile>();
    }

    
    public AbstractFile getParent() {
        return null;
    }

    
    public boolean isValid() {
        return false;
    }

    
    public boolean isDirectory() {
        return false;
    }

    
    public String getPath() {
        return null;
    }

    
    public String getUrl() {
        return null;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMappingType() {
        return mappingType;
    }

    public void setMappingType(String mappingType) {
        this.mappingType = mappingType;
    }

    public boolean isPersistent() {
        return isPersistent;
    }

    public void setPersistent(boolean persistent) {
        isPersistent = persistent;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getShortTableName() {
        return shortTableName;
    }

    public void setShortTableName(String shortTableName) {
        this.shortTableName = shortTableName;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public ArrayList<Links> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Links> links) {
        this.links = links;
    }

    public ArrayList<SolifeQuery> getQueries() {
        return queries;
    }

    public void setQueries(ArrayList<SolifeQuery> queries) {
        this.queries = queries;
    }

    @Override
    public String toString() {
        return "Persistent{" +
                "className='" + className + '\'' +
                ", name='" + name + '\'' +
                ", mappingType='" + mappingType + '\'' +
                ", isPersistent=" + isPersistent +
                ", tableName='" + tableName + '\'' +
                ", shortTableName='" + shortTableName + '\'' +
                ", fields=" + fields +
                ", links=" + links +
                ", queries=" + queries +
                '}';
    }


    public static final class Builder {
        private String className;
        private String name;
        private String mappingType;
        private boolean isPersistent;
        private String tableName;
        private String shortTableName;
        private ArrayList<Field> fields;
        private ArrayList<Links> links;
        private ArrayList<SolifeQuery> queries;

        private Builder() {
        }

        public Persistent build() {
            return new Persistent(this);
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder mappingType(String mappingType) {
            this.mappingType = mappingType;
            return this;
        }

        public Builder isPersistent(boolean isPersistent) {
            this.isPersistent = isPersistent;
            return this;
        }

        public Builder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder shortTableName(String shortTableName) {
            this.shortTableName = shortTableName;
            return this;
        }

        public Builder fields(ArrayList<Field> fields) {
            this.fields = fields;
            return this;
        }

        public Builder links(ArrayList<Links> links) {
            this.links = links;
            return this;
        }

        public Builder queries(ArrayList<SolifeQuery> queries) {
            this.queries = queries;
            return this;
        }
    }

}
