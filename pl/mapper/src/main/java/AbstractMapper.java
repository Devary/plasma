import files.AbstractFile;

import java.util.HashMap;

public class AbstractMapper implements IAbstractMapper{
    public HashMap<Integer, AbstractFile> files = null;
    public String projectPath = null;


    public void initProject() {

    }

    public void getAllFiles() {

    }

    public void getPersistenceFiles() {

    }

    public HashMap<Integer, AbstractFile> getFiles() {
        return files;
    }

    public void setFiles(HashMap<Integer, AbstractFile> files) {
        this.files = files;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
    public void setFile(AbstractFile abstractFile)
    {
        if (abstractFile != null)
        {
            this.files.put(this.files.size()+1,abstractFile);
        }
    }
}
