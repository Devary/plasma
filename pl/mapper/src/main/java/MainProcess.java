/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import files.FileTypes;
import mappers.AbstractMapper;
import projects.ProjectImpl;
import services.processing.AbstractProcess;
import services.processing.JavaClassRecursiveUpdate;
import services.processing.ProcessingTypes;
import services.reporting.Report;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class MainProcess {
    private static String basePath = "C:/Sandboxes/solife_6_1_2_CLV23_FP";
    private static Report report = new Report();

    public static void main(String[] args) {



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
        //TODO : clone classes into the project and getdeclaredFields with Class.forName()

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
}
