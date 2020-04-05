/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

import files.FileTypes;
import mappers.AbstractMapper;
import projects.ProjectFile;
import projects.ProjectImpl;
import services.processing.AbstractProcess;
import services.processing.JavaClassRecursiveUpdate;
import services.processing.JavaClassesCreationProcess;
import services.processing.ProcessingTypes;
import services.reporting.Report;

import java.util.ArrayList;

public class MainProcess {
    public static void main(String[] args) {
        //// processing java classes creation
        System.out.println(args.length);
        String basePath = "C:\\Sandboxes\\solife_6_1_2_CLV23_FP";
        Report report = new Report();
        AbstractMapper abstractMapperJava = initAbstractMapper(ProcessingTypes.JAVACLASS,FileTypes.JAVACLASS,basePath,report);
        ProjectImpl project = abstractMapperJava.getProject();
        project.setProjectJavaFiles(abstractMapperJava.getProjectFiles());
        try {
            abstractMapperJava.getProcess().adaptProcess(abstractMapperJava.getProjectFiles(),ProcessingTypes.JAVACLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ///creating persistent
        AbstractMapper abstractMapperPersistence = initAbstractMapper(ProcessingTypes.PERSISTENT,FileTypes.PERSISTENCE,basePath,report);

        ProjectImpl projectP = abstractMapperPersistence.getProject();
        projectP.setProjectPersistenceFiles(abstractMapperPersistence.getProjectFiles());

        try {
            abstractMapperPersistence.getProcess().adaptProcess(abstractMapperPersistence.getProjectFiles(),ProcessingTypes.PERSISTENT);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        JavaClassRecursiveUpdate jcru = new JavaClassRecursiveUpdate(abstractMapperJava.getProcess().getJavaClasses(),abstractMapperPersistence.getProcess().getPersistents());
        System.out.println(abstractMapperJava.getProcess().getJavaClasses().size());
        System.out.println(abstractMapperPersistence.getProcess().getPersistents().size());
        System.out.println(AbstractProcess.class.getDeclaredFields()[0]);
        ///TODO : clone classes into the project and getdeclaredFields with Class.forName()

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
