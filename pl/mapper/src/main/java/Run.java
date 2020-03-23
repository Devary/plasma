import Exceptions.ErrorCodes;
import Exceptions.FileCreationException;
import Hierarchy.Classes.JavaClass;
import Hierarchy.persistence.Persistent;
import Hierarchy.persistence.types.SolifeQuery;
import com.intellij.openapi.application.ApplicationStarter;
import com.intellij.openapi.project.Project;
import files.AbstractFile;
import files.IAbstractFile;
import org.jetbrains.annotations.TestOnly;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import projects.IProject;
import projects.ProjectFile;
import projects.ProjectImpl;
import projects.Solife;
import services.parsing.ParsingService;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Run {


    public String getCommandName() {
        return null;
    }

    public void premain(String[] strings) {

    }

    public static void main(String[] strings) throws FileCreationException {
        /*IAbstractFile f = Persistent.newPersistent().className("BillingSettings").isPersistent(true).mappingType("hierarchical").name("BillingSettings").tableName("bill_sett").build();
        AbstractFile af = AbstractFile.newAbstractFile().url("ddd").path("rezf").name("zsfrr").isValid(true).extension("persistence").build();
        SolifeQuery sq = SolifeQuery.SolifeQueryBuilder.aSolifeQuery().withClassName("frfzf").build();
        ArrayList<SolifeQuery> solifeQueries = new ArrayList<>();
        solifeQueries.add(sq);

        Persistent per = Persistent.newPersistent().className("ok").queries(new ArrayList<SolifeQuery>(solifeQueries)).build();

        System.out.println(per.toString());
        throw new FileCreationException(ErrorCodes.FILE_CREATION_EXCEPTION);

         */


        ///TODO : create project
        ProjectImpl project = createProject();
        //System.out.println(project.getName());
        //getProjectFiles test
        /*ArrayList<ProjectFile> projectFiles = project.getProjectFiles();
        for (ProjectFile x : projectFiles) {
            System.out.println(x.getPath());
        }*/
        ArrayList<ProjectFile> projectPersistenceFiles = project.getProjectPersistenceFiles();
        for (ProjectFile x : projectPersistenceFiles) {
            //System.out.println(x.getName());
        }
        //System.out.println(projectPersistenceFiles.get(0).getFileContent());


        //// test parsing system
        /*try {
            boolean doc = ParsingService.parse(projectPersistenceFiles.get(0));
            System.out.println(ParsingService.getElement("class",doc).item(0).getAttributes().getNamedItem("name").getNodeValue());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }*/
        /////IMPORTANT : creating persistent object :
        ParsingService ps = new ParsingService(projectPersistenceFiles.get(0));
        Persistent p = ps.buildPersistentFromXML();

        System.out.println(p.getClassName());
        System.out.println(p.getFields().size());
        System.out.println(p.getLinks().size());
        System.out.println(p.getQueries().size());
        System.out.println(p.getCodes().size());
        ////creating persistent
        createProjectPersistentFiles(projectPersistenceFiles,ps);
        System.out.println(p.getCodes().size());

    }

    public static ProjectImpl createProject()
    {
        ProjectImpl project = new ProjectImpl();
        project.setBasePath("C:/SolifePlasma/is");
        project.setMainDirectory((AbstractFile)createAbstractFile());
        project.setName("SOLIFE");
        return project;
    }
    public static IAbstractFile createAbstractFile()
    {
        return AbstractFile.newAbstractFile().path("C:\\sandboxes\\solife_6_1_2_CLV23_FP").name("is").build();
    }
    public static void createProjectPersistentFiles(ArrayList<ProjectFile> projectPersistenceFiles,ParsingService ps)
    {
        System.out.println("Processing Object creation");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Persistent> persistents = new ArrayList<>();
        for (ProjectFile projectFile:projectPersistenceFiles)
        {

            ps = new ParsingService(projectFile);
            Persistent persistent = ps.buildPersistentFromXML();
            persistents.add(persistent);
            System.out.println(persistent.getClassName()+" built successfully !");

        }
        System.out.println("FINISHED :: "+persistents.size()+" Objects created !");

    }

}
