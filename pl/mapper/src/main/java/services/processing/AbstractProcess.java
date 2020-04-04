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
import services.reporting.Report;

import java.util.ArrayList;

public class AbstractProcess implements IAbstractProcess {

    private final Report report;

    public ArrayList<JavaClass> getJavaClasses() {
        return javaClasses;
    }

    private ArrayList<JavaClass> javaClasses = new ArrayList<>();

    public ArrayList<Persistent> getPersistents() {
        return persistents;
    }

    private ArrayList<Persistent> persistents = new ArrayList<>();
    ProjectImpl project ;

    public AbstractProcess(String processingType, String basePath, Report report) {
        ProjectImpl project = createProject(basePath);
        AbstractMapper abstractMapper = new AbstractMapper(project, processingType);
        project.setProjectJavaFiles(abstractMapper.getProjectFiles());
        this.report = report;
        this.project = project;
    }

    @Override
    public ProjectImpl createProject(String basePath) {
        ProjectImpl project = new ProjectImpl();
        project.setBasePath(basePath);
        project.setMainDirectory((AbstractFile)createAbstractFile());
        project.setName("SOLIFE");
        return project;
    }

    @Override
    public IAbstractFile createAbstractFile() {
        return AbstractFile.newAbstractFile().path("C:\\sandboxes\\solife_6_1_2_CLV23_FP").name("is").build();
    }

    @Override
    public ArrayList<Persistent> createObjectFiles(ArrayList<ProjectFile> projectJavaFiles, Report report) {

        return null;
    }

    public void adaptProcess(ArrayList<ProjectFile> projectFiles,String processingType) throws ClassNotFoundException {
        IAbstractProcess abstractProcess;

        if (processingType.equals(ProcessingTypes.JAVACLASS))
        {
            abstractProcess= new JavaClassesCreationProcess();
            javaClasses.addAll(abstractProcess.createObjectFiles(projectFiles, this.report));

        }
        else if (processingType.equals(ProcessingTypes.PERSISTENT))
        {
            abstractProcess= new PersistenceObjectsCreationProcess();
            persistents.addAll(abstractProcess.createObjectFiles(projectFiles,this.report));
        }
        else{
            //TODO : must be checked
            //should never happen for now
            return;
        }

    }
}
