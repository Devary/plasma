package hierarchy.xpdl.types;

import java.util.Collection;

public class XpdlApplication {
    private String id;
    private Collection<XpdlFormalParameter> formalParameters;


    public XpdlApplication() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection<XpdlFormalParameter> getFormalParameters() {
        return formalParameters;
    }

    public void setFormalParameters(Collection<XpdlFormalParameter> formalParameters) {
        this.formalParameters = formalParameters;
    }



}
