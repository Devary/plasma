package services.parsing;

import hierarchy.property.PropertiesFile;
import projects.ProjectFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class PropertyParsingService extends ParsingService {

    private PropertiesFile propertiesFile;



    private ProjectFile projectFile;

    public PropertyParsingService(ProjectFile projectFile) {
        this.projectFile = projectFile;
        propertiesFile = new PropertiesFile();
        process();
    }

    public void process()
    {
            Properties properties = new Properties();
            try {
                FileInputStream fis = new FileInputStream(getProjectFile().getPath());
                properties.load(fis);
                propertiesFile.setProperties(properties);
                propertiesFile.setFileName(getProjectFile().getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public PropertiesFile getPropertiesFile() {
        return propertiesFile;
    }
    private ProjectFile getProjectFile() {
        return projectFile;
    }

}
