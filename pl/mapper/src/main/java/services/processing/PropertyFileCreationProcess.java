package services.processing;

import files.IAbstractFile;
import hierarchy.property.PropertiesFile;
import projects.ProjectFile;
import projects.ProjectImpl;
import services.parsing.PropertyParsingService;
import services.reporting.Report;

import java.util.ArrayList;
import java.util.Properties;

public class PropertyFileCreationProcess implements IAbstractProcess {


    @Override
    public ProjectImpl createProject(String basePath) {
        return null;
    }

    @Override
    public IAbstractFile createAbstractFile() {
        return null;
    }

    @Override
    public ArrayList createObjectFiles(ArrayList<ProjectFile> projectJavaFiles, Report report) {
        System.out.println("Processing Object creation");
        int cnt = 0;
        ArrayList<PropertiesFile> properties = new ArrayList<>();
        for (ProjectFile projectFile:projectJavaFiles)
        {
            PropertyParsingService ps = new PropertyParsingService(projectFile);
            PropertiesFile prop = ps.getPropertiesFile();
                properties.add(prop);
            System.out.println(projectFile.getName()+" built successfully !");
            cnt++;
        }
        System.out.println("FINISHED :: "+cnt+" Objects created !");

        return properties;
    }

}
