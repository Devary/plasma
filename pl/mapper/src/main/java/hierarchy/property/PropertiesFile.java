package hierarchy.property;

import projects.ProjectFile;

import java.util.Properties;

public class PropertiesFile extends ProjectFile implements IPropertiesFile {

    Properties properties;
    String fileName;


    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
