import Exceptions.FileCreationException;
import Hierarchy.persistence.Persistent;
import files.AbstractFile;
import files.FileTypes;
import files.IAbstractFile;
import mappers.AbstractMapper;
import projects.ProjectFile;
import projects.ProjectImpl;
import services.parsing.ParsingService;

import java.util.ArrayList;

public class Run {


    public static void main(String[] strings) throws FileCreationException {

        processPersistence();
    }

    private static void processPersistence() {
        ProjectImpl project = createProject();
        AbstractMapper abstractMapper = new AbstractMapper(project);
        project.setProjectPersistenceFiles(abstractMapper.getProjectFilesByType(FileTypes.PERSISTENCE));
        ArrayList<ProjectFile> projectPersistenceFiles = project.getProjectPersistenceFiles();
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
        return AbstractFile.newAbstractFile().path("C:/SolifePlasma").name("is").build();
    }
    public static void createProjectPersistentFiles(ArrayList<ProjectFile> projectPersistenceFiles,ParsingService ps)
    {
        System.out.println("Processing Object creation");
        /*try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
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
