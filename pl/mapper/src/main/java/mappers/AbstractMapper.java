/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package mappers;

import files.AbstractFile;
import files.FileTypes;
import projects.ProjectFile;
import projects.ProjectImpl;
import services.processing.AbstractProcess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractMapper implements IAbstractMapper{
    public HashMap<Integer, AbstractFile> files = null;
    public String projectPath = null;
    public ProjectImpl project;
    public AbstractProcess process;



    public ArrayList<ProjectFile> projectFiles = new ArrayList<>();

    public AbstractMapper(ProjectImpl project,String fileType) {
        this.files = new HashMap<Integer, AbstractFile>();
        this.projectPath = projectPath;
        this.project = project;
        getProjectFilesByType(fileType);
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
    public ArrayList<ProjectFile> getProjectFiles() {
        return projectFiles;
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

    private void getProjectFilesByType(String fileType) {
        try (Stream<Path> walk = Files.walk(Paths.get(project.getMainDirectory().getPath()))) {

            List<String> result = walk.map(Path::toString)
                    .filter(f -> f.endsWith(fileType)).collect(Collectors.toList());
            result.forEach(x->{
                File f = new File(x);
                ProjectFile pf = new ProjectFile();
                pf.setExtension(fileType.replace(".",""));
                pf.setName(f.getName());
                pf.setPath(f.getPath());
                pf.setProject(project);
                projectFiles.add(pf);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ProjectImpl getProject() {
        return project;
    }

    public void setProject(ProjectImpl project) {
        this.project = project;
    }


    public void setProcess(AbstractProcess process) {
        this.process = process;
    }

    public AbstractProcess getProcess() {
        return process;
    }
}
