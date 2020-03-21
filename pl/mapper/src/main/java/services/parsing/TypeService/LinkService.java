package services.parsing.TypeService;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import services.parsing.ParsingService;

public class LinkService {
    Node node;
    private Document xmlDocument;
    private boolean checkForObjects;

    public LinkService(Node node,Document xmlDocument) {
        this.node = node;
        this.xmlDocument = xmlDocument;
        this.checkForObjects = true;
    }

    public Class getCollectionType() {
        if (!checkForObjects)
        try {
            if (createNodeAndCheckForExistence("collectionType")!=null)
                return Class.forName(this.node
                    .getAttributes()
                    .getNamedItem("collectionType")
                    .getNodeValue());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class getElementType() {
        if (!checkForObjects)
        try {
            if (createNodeAndCheckForExistence("elementType")!=null)
            return Class.forName(this.node
                    .getAttributes()
                    .getNamedItem("elementType")
                    .getNodeValue());
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
}
