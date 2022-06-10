/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package hierarchy.persistence.types;

import hierarchy.persistence.Persistent;

public class Link extends Field {
    private Persistent parent;
    private String collectionType ;
    private String elementType;
    private String referenceIntegrityCheck;
    private String inverseName;
    private String name;
    private String dbtype;
    private String dbname;

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getReferenceIntegrityCheck() {
        return referenceIntegrityCheck;
    }

    public void setReferenceIntegrityCheck(String referenceIntegrityCheck) {
        this.referenceIntegrityCheck = referenceIntegrityCheck;
    }

    public String getInverseName() {
        return inverseName;
    }

    public void setInverseName(String inverseName) {
        this.inverseName = inverseName;
    }

    public Persistent getParent() {
        return parent;
    }

    public void setParent(Persistent parent) {
        this.parent = parent;
    }

    @Override
    public String getDbtype() {
        return dbtype;
    }

    @Override
    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    @Override
    public String getDbname() {
        return dbname;
    }

    @Override
    public void setDbname(String dbname) {
        this.dbname = dbname;
    }
}
