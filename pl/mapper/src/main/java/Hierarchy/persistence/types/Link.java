package Hierarchy.persistence.types;

public class Link extends Field {

    private String collectionType ;
    private String elementType;
    private String referenceIntegrityCheck;
    private String inverseName;


    @Override
    public String getName() {
        return super.getName();
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
}
