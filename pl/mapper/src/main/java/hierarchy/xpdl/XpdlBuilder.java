package hierarchy.xpdl;

import hierarchy.xpdl.types.XpdlWorkflowProcess;
import hierarchy.xpdl.types.*;

import java.util.Collection;
//<JavaClass>
public class XpdlBuilder {
    XpdlPackageHeader packageHeader;
    XpdlRedefinableHeader redefinableHeader;
    Collection<XpdlExternalPackage> externalPackages;
    Collection<XpdlPool> pools;
    Collection<XpdlAssociation> associations;
    Collection<XpdlArtifact> artifacts;
    XpdlConformanceClass conformanceClass;
    Collection<XpdlWorkflowProcess> workflowProcesses;
    Collection<XpdlParticipant> participants;
    Collection<XpdlExtendedAttribute> extendedAttributes;


}
