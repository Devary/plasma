/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import hierarchy.Classes.JavaClass;
import hierarchy.persistence.Persistent;
import files.AbstractFile;
import files.IAbstractFile;
import hierarchy.property.PropertiesFile;
import org.apache.log4j.Logger;
import projects.ProjectFile;
import projects.ProjectImpl;
import projects.SolifeConstants;
import services.reporting.Report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AbstractProcess implements IAbstractProcess, SolifeConstants {

    private final Report report;
    private static Logger logger = Logger.getLogger(AbstractProcess.class);


    public ArrayList<JavaClass> getJavaClasses() {
        return javaClasses;
    }

    private ArrayList<JavaClass> javaClasses = new ArrayList<>();

    public ArrayList<Persistent> getPersistents() {
        return persistents;
    }

    private ArrayList<Persistent> persistents = new ArrayList<>();

    public ArrayList<PropertiesFile> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<PropertiesFile> properties) {
        this.properties = properties;
    }

    private ArrayList<PropertiesFile> properties = new ArrayList<>();
    ProjectImpl project ;

    public AbstractProcess(String processingType, String basePath, Report report) {
        ProjectImpl project = createProject(basePath);
        //todo:to be checked
        //AbstractMapper abstractMapper = new AbstractMapper(project, processingType);
        //project.setProjectJavaFiles(abstractMapper.getProjectFiles());
        this.report = report;
        this.project = project;
    }
    public AbstractProcess(Report report) {

        this.report = report;
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
        return AbstractFile.newAbstractFile().path(PROJECT_PATH).name("\\modules").build();
    }

    @Override
    public ArrayList<JavaClass> createObjectFiles(ArrayList<ProjectFile> projectJavaFiles, Report report) throws Exception {
        return null;
    }


    public void adaptProcess(ArrayList<ProjectFile> projectFiles,String processingType) throws Exception {
        IAbstractProcess abstractProcess;

        if (processingType.equals(ProcessingTypes.JAVACLASS))
        {
            abstractProcess= new JavaClassesCreationProcess();
            javaClasses.addAll(abstractProcess.createObjectFiles(projectFiles, this.report));

        }
        else if (processingType.equals(ProcessingTypes.PERSISTENT))
        {
            //for target folder
            String separator = "\\";
            ArrayList<ProjectFile> newProjectFiles = (ArrayList<ProjectFile>) projectFiles.stream().filter(projectFile -> {
                String[] splits = projectFile.getPath().split(Pattern.quote(separator));
                return Arrays.asList(splits).contains("target");
            }).collect(Collectors.toList());
            abstractProcess= new PersistenceObjectsCreationProcess();
            persistents.addAll(abstractProcess.createObjectFiles(newProjectFiles,this.report));
        }
        else if (processingType.equals(ProcessingTypes.PROPERTY))
        {
            abstractProcess= new PropertyFileCreationProcess();
            properties.addAll(abstractProcess.createObjectFiles(projectFiles,this.report));
        }
        else{
            //TODO : must be checked
            //should never happen for now
            return;
        }
        logger.warn("Init completed for : "+ processingType);
    }
}
