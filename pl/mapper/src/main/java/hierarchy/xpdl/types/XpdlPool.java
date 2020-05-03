package hierarchy.xpdl.types;

import java.util.Collection;

public class XpdlPool {
    String id;
    String name;
    String process;
    boolean isBoundaryVisible;
    Collection<XpdlLane> lanes;
    Collection<XpdlNodeGraphicsInfo> xpdlNodeGraphicsInfos;
}
