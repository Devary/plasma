/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package hierarchy.persistence;

import hierarchy.persistence.types.Code;
import files.AbstractFile;
import hierarchy.persistence.types.Field;
import hierarchy.persistence.types.Link;
import hierarchy.persistence.types.SolifeQuery;
import projects.ProjectFile;
import services.processing.validation.IValidator;
import services.processing.validation.PersistentValidator;
import services.processing.validation.ValidatorImpl;

import java.util.ArrayList;

public class Persistent extends ProjectFile implements IPersistent {

    private long id;
    private String className;
    private String name;
    private String mappingType;
    private boolean isPersistent;
    private String tableName;
    private String shortTableName;
    private ArrayList<Field> fields;
    private ArrayList<Link> links;
    private ArrayList<SolifeQuery> queries;
    private ArrayList<Code> codes;
    private String table;


    private Persistent(Builder builder) {
        this.id = builder.id;
        this.className = builder.className;
        this.name = builder.name;
        this.mappingType = builder.mappingType;
        this.isPersistent = builder.isPersistent;
        this.tableName = builder.tableName;
        this.shortTableName = builder.shortTableName;
        this.fields = builder.fields;
        this.links = builder.links;
        this.queries = builder.queries;
        this.codes = builder.codes;
        this.table = builder.table;

    }

    public static Builder newPersistent() {
        return new Builder();
    }

    
    public String getName() {
        return this.name;
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
        ValidatorImpl validator = new PersistentValidator();
        return validator.validate(this);
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

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public ArrayList<SolifeQuery> getQueries() {
        return queries;
    }

    public void setQueries(ArrayList<SolifeQuery> queries) {
        this.queries = queries;
    }
    public ArrayList<Code> getCodes() {
        return codes;
    }

    public void setCodes(ArrayList<Code> codes) {
        this.codes = codes;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }


    public static final class Builder {
        private long id;
        private String className;
        private String name;
        private String mappingType;
        private boolean isPersistent;
        private String tableName;
        private String shortTableName;
        private ArrayList<Field> fields;
        private ArrayList<Link> links;
        private ArrayList<SolifeQuery> queries;
        private ArrayList<Code> codes;
        private String table;

        private Builder() {
        }

        public Persistent build() {
            return new Persistent(this);
        }

        public Builder id(long id) {
            this.id = id;
            return this;
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

        public Builder links(ArrayList<Link> links) {
            this.links = links;
            return this;
        }

        public Builder queries(ArrayList<SolifeQuery> queries) {
            this.queries = queries;
            return this;
        }

        public Builder codes(ArrayList<Code> codeList) {
            this.codes = codeList;
            return this;
        }

        public Builder table(String table) {
            this.table = table;
            return this;
        }
    }


}
