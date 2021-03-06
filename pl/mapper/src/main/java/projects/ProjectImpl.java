/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

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



    private ArrayList<ProjectFile> projectPropertiesFiles = new ArrayList<>();


    private ArrayList<ProjectFile> projectJavaFiles = new ArrayList<>();
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
        return projectPersistenceFiles;
    }

    public void setProjectPersistenceFiles(ArrayList<ProjectFile> projectPersistenceFiles) {
        this.projectPersistenceFiles = projectPersistenceFiles;
    }

    public ArrayList<ProjectFile> getProjectJavaFiles() {
        return projectJavaFiles;
    }

    public void setProjectJavaFiles(ArrayList<ProjectFile> projectJavaFiles) {
        this.projectJavaFiles = projectJavaFiles;
    }

    public void setProjectPropertiesFiles(ArrayList<ProjectFile> projectPropertiesFiles) {
        this.projectPropertiesFiles = projectPropertiesFiles;
    }

}
