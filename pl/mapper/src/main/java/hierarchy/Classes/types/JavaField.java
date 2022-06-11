/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package hierarchy.Classes.types;

public class JavaField {
    private final String collectionType;
    private String name ;
    private String type ;
    private boolean isCollection ;

    public JavaField(String name, String type,boolean isCollection,String collectionType) {
        this.name = name;
        this.type = type;
        this.isCollection = isCollection;
        this.collectionType = collectionType;
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
}
