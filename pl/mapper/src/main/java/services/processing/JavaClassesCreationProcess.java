/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import Hierarchy.Classes.JavaClass;
import files.IAbstractFile;
import projects.ProjectFile;
import projects.ProjectImpl;
import services.parsing.JavaClassesParsingService;
import services.reporting.Report;

import java.util.ArrayList;
import java.util.Collection;

public class JavaClassesCreationProcess implements IAbstractProcess {


    private Collection<JavaClass> javaClasses ;

    public Collection<JavaClass> getJavaClasses() {
        return javaClasses;
    }

    private void setJavaClasses(Collection<JavaClass> javaClasses) {
        this.javaClasses = javaClasses;
    }

    @Override
    public ProjectImpl createProject(String basePath) {
        return null;
    }

    @Override
    public IAbstractFile createAbstractFile() {
        return null;
    }

    @Override
    public ArrayList<JavaClass> createObjectFiles(ArrayList<ProjectFile> projectJavaFiles, Report report) {
        System.out.println("Processing Object creation");

        ArrayList<JavaClass> javaClasses = new ArrayList<>();
        for (ProjectFile projectFile:projectJavaFiles)
        {
            JavaClassesParsingService ps = new JavaClassesParsingService(projectFile,report);
            JavaClass javaClass = ps.getJavaClass();
            if (javaClass.getClassName()!=null)
            javaClasses.add(javaClass);
            System.out.println(javaClass.getClassName()+" built successfully !");
        }
        System.out.println("FINISHED :: "+javaClasses.size()+" Objects created !");
        this.javaClasses = javaClasses;
        return javaClasses;
    }
}
