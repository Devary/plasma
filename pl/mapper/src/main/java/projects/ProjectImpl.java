package projects;

import files.AbstractFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectImpl extends AbstractProject implements IProject {

    private ArrayList<ProjectFile> projectFiles = new ArrayList<>();
    private ArrayList<ProjectFile> projectPersistenceFiles = new ArrayList<>();
    @Override
    public ArrayList<ProjectFile> getProjectFiles() {

        try (Stream<Path> walk = Files.walk(Paths.get(this.getMainDirectory().getPath()))) {

            List<String> result = walk.filter(Files::isDirectory)
                    .map(Path::toString).collect(Collectors.toList());

            result.forEach(x->{
                ProjectFile pf = new ProjectFile();
                pf.setPath(x);
                projectFiles.add(pf);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectFiles;
    }
    public ArrayList<ProjectFile> getProjectPersistenceFiles() {

        try (Stream<Path> walk = Files.walk(Paths.get(this.getMainDirectory().getPath()))) {

            List<String> result = walk.map(Path::toString)
                    .filter(f -> f.endsWith(".persistence")).collect(Collectors.toList());
            result.forEach(x->{
                File f = new File(x);
                ProjectFile pf = new ProjectFile();
                pf.setExtension("persistence");
                pf.setName(f.getName());
                pf.setPath(f.getPath());
                pf.setProject(this);
                projectPersistenceFiles.add(pf);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectPersistenceFiles;
    }

}
