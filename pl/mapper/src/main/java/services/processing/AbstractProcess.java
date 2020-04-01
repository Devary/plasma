/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import Hierarchy.Classes.JavaClass;
import Hierarchy.persistence.Persistent;
import files.AbstractFile;
import files.IAbstractFile;
import mappers.AbstractMapper;
import projects.ProjectFile;
import projects.ProjectImpl;

import java.util.ArrayList;

public class AbstractProcess implements IAbstractProcess {

    public ArrayList<JavaClass> getJavaClasses() {
        return javaClasses;
    }

    private ArrayList<JavaClass> javaClasses = new ArrayList<>();

    public ArrayList<Persistent> getPersistents() {
        return persistents;
    }

    private ArrayList<Persistent> persistents = new ArrayList<>();
    ProjectImpl project ;

    public AbstractProcess(String processingType) {
        ProjectImpl project = createProject();
        AbstractMapper abstractMapper = new AbstractMapper(project, processingType);
        project.setProjectJavaFiles(abstractMapper.getProjectFiles());
        ArrayList<ProjectFile> projectJavaFiles = project.getProjectJavaFiles();
        this.project = project;
    }

    @Override
    public ProjectImpl createProject() {
        ProjectImpl project = new ProjectImpl();
        project.setBasePath("C:/SolifePlasma/is");
        project.setMainDirectory((AbstractFile)createAbstractFile());
        project.setName("SOLIFE");
        return project;
    }

    @Override
    public IAbstractFile createAbstractFile() {
        return AbstractFile.newAbstractFile().path("C:/SolifePlasma").name("is").build();
    }

    @Override
    public ArrayList<Persistent> createObjectFiles(ArrayList<ProjectFile> projectJavaFiles) {

        return null;
    }

    public void adaptProcess(ArrayList<ProjectFile> projectFiles,String processingType) throws ClassNotFoundException {
        IAbstractProcess abstractProcess;

        if (processingType.equals(ProcessingTypes.JAVACLASS))
        {
            abstractProcess= new JavaClassesCreationProcess();
            javaClasses.addAll(abstractProcess.createObjectFiles(projectFiles));

        }
        else if (processingType.equals(ProcessingTypes.PERSISTENT))
        {
            abstractProcess= new PersistenceObjectsCreationProcess();
            persistents.addAll(abstractProcess.createObjectFiles(projectFiles));
        }
        else{
            //TODO : must be checked
            //should never happen for now
            abstractProcess = new AbstractProcess(ProcessingTypes.JAVACLASS);
        }

    }
}
