import files.AbstractFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractMapper implements IAbstractMapper{
    public HashMap<Integer, AbstractFile> files = null;
    public String projectPath = null;

    public AbstractMapper(String projectPath) {
        this.files = new HashMap<Integer, AbstractFile>();
        this.projectPath = projectPath;
    }


    public void initProject() {
        projectPath= "C:\\projects";
    }

    public void getAllFiles() {
        try (Stream<Path> walk = Files.walk(Paths.get(projectPath))) {
            List<AbstractFile> result = walk.filter(Files::isRegularFile)
                    .map(x -> {
                        //TODO : need file builder and build custom file AbstractFile from java.io.File
                        return new AbstractFile();
                    }).collect(Collectors.toList());
            result.forEach(x -> {this.files.put(this.files.size()+1,x);});
        } catch (IOException e) {
            e.printStackTrace();
        }
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
