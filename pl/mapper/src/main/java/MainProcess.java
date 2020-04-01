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

import java.util.ArrayList;

public class MainProcess {
    public static void main(String[] args) {
        //// processing java classes creation
        AbstractMapper abstractMapperJava = initAbstractMapper(ProcessingTypes.JAVACLASS,FileTypes.JAVACLASS);
        ProjectImpl project = abstractMapperJava.getProject();
        project.setProjectJavaFiles(abstractMapperJava.getProjectFiles());
        try {
            abstractMapperJava.getProcess().adaptProcess(abstractMapperJava.getProjectFiles(),ProcessingTypes.JAVACLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ///creating persistent
        AbstractMapper abstractMapperPersistence = initAbstractMapper(ProcessingTypes.PERSISTENT,FileTypes.PERSISTENCE);

        ProjectImpl projectP = abstractMapperPersistence.getProject();
        projectP.setProjectPersistenceFiles(abstractMapperPersistence.getProjectFiles());

        try {
            abstractMapperPersistence.getProcess().adaptProcess(abstractMapperPersistence.getProjectFiles(),ProcessingTypes.PERSISTENT);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        JavaClassRecursiveUpdate jcru = new JavaClassRecursiveUpdate(abstractMapperJava.getProcess().getJavaClasses());
        System.out.println(abstractMapperJava.getProcess().getJavaClasses().size());
        System.out.println(abstractMapperPersistence.getProcess().getPersistents().size());


    }

    private static AbstractMapper initAbstractMapper(String processingType, String fileType) {
        AbstractProcess abstractProcess= new AbstractProcess(processingType);
        ProjectImpl project = abstractProcess.createProject();
        AbstractMapper abstractMapper = new AbstractMapper(project, fileType);
        abstractMapper.setProject(project);
        abstractMapper.setProcess(abstractProcess);
        return abstractMapper;
    }
}
