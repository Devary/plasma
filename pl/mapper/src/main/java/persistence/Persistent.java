package persistence;

import files.AbstractFile;
import persistence.types.Field;
import persistence.types.Links;
import persistence.types.SolifeQuery;

import java.util.ArrayList;

public class Persistent implements IPersistent {

    public String className;
    public String name;
    public String mappingType;
    public boolean isPersistent;
    public String tableName;
    public String shortTableName;
    public ArrayList<Field> fields;
    public ArrayList<Links> links;
    public ArrayList<SolifeQuery> queries;





    
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

    
    public AbstractFile[] getChildren() {
        return new AbstractFile[0];
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

    public static class PersistentBuilder
    {
        public String className;
        public String name;
        public String mappingType;
        public boolean isPersistent;
        public String tableName;
        public String shortTableName;
        public ArrayList<Field> fields;
        public ArrayList<Links> links;
        public ArrayList<SolifeQuery> queries;

        public PersistentBuilder(String className) {
            //this.className = firstName;
            //this.lastName = lastName;
        }
        public PersistentBuilder className(String className) {
            this.className = className;
            return this;
        }

        //Return the finally consrcuted User object
        public Persistent build() {
           // Persistent persistent =  new Persistent(this);
           // validateUserObject(persistent);
           // return persistent;
            return new Persistent();
        }
        private void validateUserObject(Persistent persistent) {
            //Do some basic validations to check

            //if user object does not break any assumption of system
        }
    }

}
