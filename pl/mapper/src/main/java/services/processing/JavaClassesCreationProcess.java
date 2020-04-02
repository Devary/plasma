/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import Hierarchy.Classes.JavaClass;
import Hierarchy.persistence.Persistent;
import files.IAbstractFile;
import projects.ProjectFile;
import projects.ProjectImpl;
import services.parsing.JavaClassesParsingService;

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
    public ProjectImpl createProject() {
        return null;
    }

    @Override
    public IAbstractFile createAbstractFile() {
        return null;
    }

    @Override
    public ArrayList<JavaClass> createObjectFiles(ArrayList<ProjectFile> projectJavaFiles) {
        System.out.println("Processing Object creation");

        ArrayList<JavaClass> javaClasses = new ArrayList<>();
        for (ProjectFile projectFile:projectJavaFiles)
        {
            JavaClassesParsingService ps = new JavaClassesParsingService(projectFile);
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
