/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

import files.FileTypes;
import hierarchy.Classes.JavaClass;
import hierarchy.persistence.Persistent;
import mappers.AbstractMapper;
import projects.ProjectImpl;
import services.processing.AbstractProcess;
import services.processing.JavaClassRecursiveUpdate;
import services.processing.ProcessingTypes;
import services.reporting.Report;

import java.util.ArrayList;

public class MainProcess {
    private static String basePath = "C:/Sandboxes/solife_6_1_2_CLV23_FP";
    private static Report report = new Report();

    public void setJavaClasses(ArrayList<JavaClass> javaClasses) {
        this.javaClasses = javaClasses;
    }

    public ArrayList<JavaClass> javaClasses = null;

    public void setPeristents(ArrayList<Persistent> peristents) {
        this.peristents = peristents;
    }

    public ArrayList<Persistent> getPeristents() {
        return peristents;
    }

    public ArrayList<Persistent> peristents = null;
    public  MainProcess(){
        AbstractMapper abstractMapperJava = initAbstractMapper(ProcessingTypes.JAVACLASS,FileTypes.JAVACLASS,basePath,report);


        p1(abstractMapperJava);

        //creating persistent
        AbstractMapper abstractMapperPersistence = initAbstractMapper(ProcessingTypes.PERSISTENT,FileTypes.PERSISTENCE,basePath,report);

        p2(abstractMapperPersistence);

        //properties
        AbstractMapper abstractMapperProperties = initAbstractMapper(ProcessingTypes.PROPERTY,FileTypes.PROPERTIES,basePath,report);
        p3(abstractMapperProperties);
        System.out.println("ok");

        // updating JAVACLASSES
        p4(abstractMapperJava,abstractMapperPersistence,abstractMapperProperties);

        setJavaClasses(abstractMapperJava.getProcess().getJavaClasses());
        setPeristents(abstractMapperPersistence.getProcess().getPersistents());

    }

    private static void p3(AbstractMapper abstractMapperProperties) {
        ProjectImpl projectP2 = abstractMapperProperties.getProject();
        projectP2.setProjectPropertiesFiles(abstractMapperProperties.getProjectFiles());

        try {
            abstractMapperProperties.getProcess().adaptProcess(abstractMapperProperties.getProjectFiles(),ProcessingTypes.PROPERTY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void p4(AbstractMapper abstractMapperJava, AbstractMapper abstractMapperPersistence, AbstractMapper abstractMapperProperties) {
        JavaClassRecursiveUpdate jcru = new JavaClassRecursiveUpdate(
                abstractMapperJava.getProcess().getJavaClasses(),
                abstractMapperPersistence.getProcess().getPersistents(),
                abstractMapperProperties.getProcess().getProperties()
        );
        System.out.println(abstractMapperJava.getProcess().getJavaClasses().size());
        System.out.println(abstractMapperPersistence.getProcess().getPersistents().size());
        System.out.println(AbstractProcess.class.getDeclaredFields()[0]);
    }

    private static void p2(AbstractMapper abstractMapperPersistence) {

        ProjectImpl projectP = abstractMapperPersistence.getProject();
        projectP.setProjectPersistenceFiles(abstractMapperPersistence.getProjectFiles());

        try {
            abstractMapperPersistence.getProcess().adaptProcess(abstractMapperPersistence.getProjectFiles(),ProcessingTypes.PERSISTENT);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void p1(AbstractMapper abstractMapperJava) {
        ProjectImpl project = abstractMapperJava.getProject();
        project.setProjectJavaFiles(abstractMapperJava.getProjectFiles());
        try {
            abstractMapperJava.getProcess().adaptProcess(abstractMapperJava.getProjectFiles(),ProcessingTypes.JAVACLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static AbstractMapper initAbstractMapper(String processingType, String fileType,String basePath,Report report) {
        AbstractProcess abstractProcess= new AbstractProcess(processingType,basePath,report);
        ProjectImpl project = abstractProcess.createProject(basePath);
        AbstractMapper abstractMapper = new AbstractMapper(project, fileType);
        abstractMapper.setProject(project);
        abstractMapper.setProcess(abstractProcess);
        return abstractMapper;
    }

    public ArrayList<JavaClass> getJavaClasses() {
        return this.javaClasses;
    }
}
