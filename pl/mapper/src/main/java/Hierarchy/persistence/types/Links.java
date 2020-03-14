package Hierarchy.persistence.types;

public class Links extends Field {

    private Class collectionType ;
    private Class elementType;
    private String referenceIntegrityCheck;
    private String inverseName;


    @Override
    public String getName() {
        return super.getName();
    }

    public Class getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(Class collectionType) {
        this.collectionType = collectionType;
    }

    public Class getElementType() {
        return elementType;
    }

    public void setElementType(Class elementType) {
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
}
