package hierarchy.xpdl.types;

import java.util.Collection;

public class XpdlWorkflowProcess {
    private String id;
    private String name;
    private XpdlProcessHeader processHeader;
    private Collection<XpdlFormalParameter> formalParameters;
    private Collection<XpdlApplication> applications;
    private Collection<XpdlDataField> dataFields;
    private Collection<XpdlActivity> activities;
}
