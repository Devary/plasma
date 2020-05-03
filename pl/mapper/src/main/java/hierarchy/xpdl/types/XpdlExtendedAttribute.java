package hierarchy.xpdl.types;

public class XpdlExtendedAttribute {
    private String name;
    private String value;

    public XpdlExtendedAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
