package services.parsing.TypeService;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import projects.ProjectFile;
import services.parsing.ParsingService;

public class LinkService extends ParsingService{
    Node node;
    private Document xmlDocument;
    private boolean checkForObjects;
    private ProjectFile projectFile = null;

    public LinkService(Node node,Document xmlDocument) {
        this.node = node;
        this.xmlDocument = xmlDocument;
        this.checkForObjects = true;
        this.projectFile= getFile();

    }

    public String getCollectionType() {
        if (!checkForObjects)
            if (createNodeAndCheckForExistence("collectionType")!=null)
            {
                return this.node
                        .getAttributes()
                        .getNamedItem("collectionType")
                        .getNodeValue();
            }
        return null;
    }

    public String getElementType() {
        if (!checkForObjects)
            if (createNodeAndCheckForExistence("elementType")!=null)
                return this.node
                        .getAttributes()
                        .getNamedItem("elementType")
                        .getNodeValue();
        return null;
    }

    public String getInverseName() {
        return createNodeAndCheckForExistence("inverseName");
    }

    public String getIfReferenceIntegrityCheck() {
        return createNodeAndCheckForExistence("referenceIntegrityCheck");
    }

    public boolean getAllowsNull() {
        return Boolean.parseBoolean(createNodeAndCheckForExistence("allowsNull"));
    }
    private String createNodeAndCheckForExistence(String nodeName) {
        Node node = this.node
                .getAttributes()
                .getNamedItem(nodeName);
        if (checkIfNodeExists(node)) {
            return node.getNodeValue();
        }
        return null;
    }
    private boolean checkIfNodeExists(Node node) {
        return null != node;
    }

    @Override
    public Class loadClassFromTargetProject(ProjectFile projectFile, String classPath) {
        return super.loadClassFromTargetProject(projectFile, classPath);
    }
}
