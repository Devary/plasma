/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package hierarchy.Classes.types;

public class JavaField {
    private final String collectionType;
    private String name ;
    private String type ;
    private boolean isCollection ;
    private long classId;
    private long id;

    public JavaField(String name, String type,boolean isCollection,String collectionType) {
        this.name = name;
        this.type = type;
        this.isCollection = isCollection;
        this.collectionType = collectionType;
    }
    public JavaField(int id, String name, String type, boolean isCollection, String collectionType, long classId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isCollection = isCollection;
        this.collectionType = collectionType;
        this.classId = classId;
    }

    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public long getId() {
        return id;
    }
}
