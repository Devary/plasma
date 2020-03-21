package services.parsing.TypeService;

import org.w3c.dom.Node;
import projects.ProjectFile;
import services.parsing.ParsingService;

public class FieldService extends ParsingService{
    Node node= null;
    ProjectFile projectFile;
    public FieldService(Node node) {
        this.node = node;
        this.projectFile= getFile();

    }
    /// TODO : optimization node
    //                .getAttributes() must be replaced
    public String getShortTableName() {
        return createNodeAndCheckForExistence("shortTableName");
    }

    public boolean getIfAllowsNull() {
        return Boolean.parseBoolean(createNodeAndCheckForExistence("allowNulls"));
    }

    public String getName() {
        return createNodeAndCheckForExistence("name");
    }
    public String getFieldName() {
        return createNodeAndCheckForExistence("fieldName");
    }
    public String getDbName() {
        return createNodeAndCheckForExistence("dbname");
    }
    public String getDbType() {
        return createNodeAndCheckForExistence("dbtype");
    }
    public String getDbSize() {
        return createNodeAndCheckForExistence("dbsize");
    }
    //TODO : dbtype

    public String getDefaultValue() {
        return createNodeAndCheckForExistence("defaultValue");
    }

    public String getQueryClassName() {
        return createNodeAndCheckForExistence("class");
    }

    public String getResultType() {
        if (createNodeAndCheckForExistence("resultType")!=null)
        {
            return this.node
                    .getAttributes()
                    .getNamedItem("resultType")
                    .getNodeValue();
        }
        return null;
    }

    public boolean getIdentity() {
        return Boolean.parseBoolean(createNodeAndCheckForExistence("identity"));
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
