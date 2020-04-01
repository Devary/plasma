/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.parsing;

import Hierarchy.persistence.Persistent;
import Hierarchy.persistence.types.Code;
import Hierarchy.persistence.types.Field;
import Hierarchy.persistence.types.Link;
import Hierarchy.persistence.types.SolifeQuery;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import projects.ProjectFile;
import services.parsing.TypeService.FieldService;
import services.parsing.TypeService.JsonParsingService;
import services.parsing.TypeService.LinkService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;

public class ParsingService {

    private static Document xmlDocument;
    private JsonParsingService jsonParsingService;
    private Persistent persistent = Persistent.newPersistent().build();
    public ProjectFile getFile() {
        return file;
    }

    public void setFile(ProjectFile file) {
        this.file = file;
    }

    public ProjectFile file;

    static {
        xmlDocument = null;
    }

    public ParsingService(ProjectFile file) {
        this.file=file;
        jsonParsingService = new JsonParsingService();
        try {
            parse();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public ParsingService() {
    }

    public boolean parse() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        xmlDocument = builder.parse(new InputSource(new StringReader(file.getFileContent())));
        return true;
    }
    public static NodeList getElement(String elementName, Document doc)
    {
        return doc.getElementsByTagName(elementName);
    }

    public Persistent buildPersistentFromXML()
    {
        persistent = Persistent.newPersistent()
                .className(getClassName())
                .tableName(getTableName(getClassName()))
                .isPersistent(Boolean.parseBoolean(getIsPersistent()))
                .mappingType(getMappingType())
                .shortTableName(getShortTableName())
                .fields(getFieldList())
                .codes(getCodeList())
                .queries(getQueryList())
                .links(getLinkList())
                .build();

        return persistent;
    }

    private ArrayList<Link> getLinkList() {
        NodeList nl = ParsingService.getElement("link",xmlDocument);
        ArrayList<Link> links = new ArrayList<>();
        for (int i = 0; i < nl.getLength(); i++) {
            LinkService ls =new LinkService(nl.item(i));
            Link link = new Link();
            link.setCollectionType(ls.getCollectionType());
            link.setElementType(ls.getElementType());
            link.setInverseName(ls.getInverseName());
            link.setReferenceIntegrityCheck(ls.getIfReferenceIntegrityCheck());
            link.setAllowNulls(ls.getAllowsNull());
            link.setParent(persistent);
            links.add(link);
        }
        return links;
    }

    private ArrayList<SolifeQuery> getQueryList() {
        NodeList nl = ParsingService.getElement("query",xmlDocument);
        ArrayList<SolifeQuery> queries = new ArrayList<>();
        for (int i = 0; i < nl.getLength(); i++) {
            FieldService fs =new FieldService(nl.item(i));
            queries.add(SolifeQuery.newSolifeQuery()
                    .name(fs.getName())
                    .className(fs.getQueryClassName())
                    .identity(fs.getIdentity())
                    .resultType(fs.getResultType())
                    .build());
        }
        return queries;
    }

    private ArrayList<Field> getFieldList() {
        NodeList nl = ParsingService.getElement("field",xmlDocument);
        ArrayList<Field> fields = new ArrayList<>();
        for (int i = 0; i < nl.getLength(); i++) {
            Field f = new Field();
            FieldService fs =new FieldService(nl.item(i));
            f.setName(fs.getName());
            f.setAllowNulls(fs.getIfAllowsNull());
            f.setDbname(getTableName(fs.getName()));
            f.setDbtype(fs.getDbType());
            f.setDbsize(fs.getDbSize());
            f.setDefaultValue(fs.getDefaultValue());
            fields.add(f);
        }
        return fields;
    }

    private ArrayList<Code> getCodeList() {
        NodeList nl = ParsingService.getElement("code",xmlDocument);
        ArrayList<Code> codes = new ArrayList<>();
        for (int i = 0; i < nl.getLength(); i++) {
            Code c = new Code();
            FieldService fs =new FieldService(nl.item(i));
            c.setName(fs.getName());
            c.setDbname(fs.getDbName());
            codes.add(c);
        }
        return codes;
    }
    private String getShortTableName() {
        return createNodeAndCheckForExistence("class","shortTableName");

    }

    private boolean checkIfNodeExists(Node node) {
        return null != node;
    }

    private String getIsPersistent() {
        return createNodeAndCheckForExistence("class","persistent");

    }

    private String createNodeAndCheckForExistence(String elementName,String nodeName) {
        Node node = ParsingService.getElement(elementName,xmlDocument)
                .item(0)
                .getAttributes()
                .getNamedItem(nodeName);
        if (checkIfNodeExists(node)) {
            return node.getNodeValue();
        }
        return null;
    }

    public static String getTableName(String name) {
        if (moreThanZeroUpperCaseLetter(name))
        {
            String newName = name.replaceAll("([A-Z])", "_$1").toLowerCase();
            correctDBname(newName);
            return newName;
        }
        else
        {
            String newName = name.toLowerCase();
            correctDBname(newName);
            return newName;
        }
    }
    private static boolean moreThanZeroUpperCaseLetter(String s) {
        return s.codePoints().filter(c-> c>='A' && c<='Z').count()>0;
    }
    private static void correctDBname(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (i!=0 && i+1<s.length())
            {
                //TODO : external_i_d
                char prevch = s.charAt(i-1);
                int prevasciivalue = (int)prevch;
                char nextch = s.charAt(i+1);
                int nextasciivalue = (int)nextch;
                if (prevasciivalue==95 && nextasciivalue ==95)
                {
                    s.substring(0,i+1);
                }
            }
        }
    }

    private String getClassName()
    {
        return createNodeAndCheckForExistence("class","name");
    }
    private String getMappingType()
    {
        return createNodeAndCheckForExistence("class","mapping");
    }

    public Class loadClassFromTargetProject(ProjectFile projectFile,String classPath)throws Exception
    {
        /*String filePathAndName = this.file.getProject().getBasePath()+"/created_classes/"+file.getName();
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePathAndName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{

            File file = new File(projectFile.getProject().getBasePath());

            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};

            ClassLoader cl = new URLClassLoader(urls);
            Class cls = cl.loadClass(classPath);
            File newFile = new File(filePathAndName);
            ProtectionDomain pDomain = cls.getProtectionDomain();
            CodeSource cSource = pDomain.getCodeSource();
            if (newFile.createNewFile())
                if (writer != null) {
                    writer.write(cSource.toString());
                    writer.close();
                }
            return newFile.getClass();

            //ProtectionDomain pDomain = cls.getProtectionDomain();
            //CodeSource cSource = pDomain.getCodeSource();
            //URL urlfrom = cSource.getLocation();
            //System.out.println(urlfrom.getFile());

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;*/
        return Class.forName("ok");
    }

}
