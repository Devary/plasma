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

    public static void main(String[] args) throws Exception {
        new MainProcess();
    }
    private void setJavaClasses(ArrayList<JavaClass> javaClasses) {
        this.javaClasses = javaClasses;
    }

    private ArrayList<JavaClass> javaClasses = null;

    private void setPersistents(ArrayList<Persistent> persistents) {
        this.peristents = persistents;
    }

    ArrayList<Persistent> getPeristents() {
        return peristents;
    }

    private ArrayList<Persistent> peristents = null;
    MainProcess() throws Exception {
        AbstractMapper abstractMapperPersistence = initAbstractMapper(ProcessingTypes.PERSISTENT,FileTypes.PERSISTENCE,basePath,report);

        p2(abstractMapperPersistence);

        setPersistents(abstractMapperPersistence.getProcess().getPersistents());

        AbstractMapper abstractMapperJava = initAbstractMapper(ProcessingTypes.JAVACLASS,FileTypes.JAVACLASS,basePath,report);


        p1(abstractMapperJava);

        setJavaClasses(abstractMapperJava.getProcess().getJavaClasses());
//creating persistent



        //properties
        //AbstractMapper abstractMapperProperties = initAbstractMapper(ProcessingTypes.PROPERTY,FileTypes.PROPERTIES,basePath,report);
        //p3(abstractMapperProperties);
        //System.out.println("ok");

         //updating JAVACLASSES
        //p4(abstractMapperJava,abstractMapperPersistence,abstractMapperProperties);
        p4(abstractMapperJava,abstractMapperPersistence,null);

        //

    }

    private static void p3(AbstractMapper abstractMapperProperties) throws Exception {
        ProjectImpl projectP2 = abstractMapperProperties.getProject();
        //projectP2.setProjectPropertiesFiles(abstractMapperProperties.getProjectFiles());

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
                //abstractMapperProperties.getProcess().getProperties()
                null
        );
        System.out.println(abstractMapperJava.getProcess().getJavaClasses().size());
        System.out.println(abstractMapperPersistence.getProcess().getPersistents().size());
        System.out.println(AbstractProcess.class.getDeclaredFields()[0]);
    }

    private static void p2(AbstractMapper abstractMapperPersistence) throws Exception {

        ProjectImpl projectP = abstractMapperPersistence.getProject();
        //projectP.setProjectPersistenceFiles(abstractMapperPersistence.getProjectFiles());

        try {
            abstractMapperPersistence.getProcess().adaptProcess(abstractMapperPersistence.getProjectFiles(),ProcessingTypes.PERSISTENT);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void p1(AbstractMapper abstractMapperJava) throws Exception {
        ProjectImpl project = abstractMapperJava.getProject();
        //project.setProjectJavaFiles(abstractMapperJava.getProjectFiles());
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
