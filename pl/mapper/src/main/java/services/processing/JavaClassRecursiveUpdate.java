/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import Hierarchy.Classes.JavaClass;
import files.FileTypes;
import mappers.AbstractMapper;
import projects.ProjectFile;
import projects.ProjectImpl;
import services.parsing.JavaClassesParsingService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.UnaryOperator;

public class JavaClassRecursiveUpdate {

    private ArrayList<JavaClass> javaclasses;

    public JavaClassRecursiveUpdate(ArrayList<JavaClass> javaclasses) {
        this.javaclasses = javaclasses;
        update();
    }

    private void update()
     {
         for (JavaClass javaClass:javaclasses)
         {
             updateImpl(javaClass);
             updateExt(javaClass);
         }
     }

    private void updateImpl(JavaClass javaClass) {
        if (javaClass.getImplementations()==null)
        {
            return;
        }
        ArrayList<JavaClass> newList = new ArrayList<>();
        for (JavaClass jc : javaClass.getImplementations()) {
            JavaClass found = find(jc);

            if (found != null) {
                newList.add(found);
            } else {
                newList.add(jc);
            }
        }
        javaClass.setImplementations(newList);
    }
    private void updateExt(JavaClass javaClass) {
        if (javaClass.getHeritances()==null)
        {
            return;
        }
        ArrayList<JavaClass> newList = new ArrayList<>();
        for (JavaClass jc : javaClass.getHeritances()) {
            JavaClass found = find(jc);

            if (found != null) {
                newList.add(found);
            } else {
                newList.add(jc);
            }
        }
        javaClass.setHeritances(newList);
    }

    private JavaClass find(JavaClass jc) {
       if (jc.getClassName()!=null)
       {
           for (JavaClass javaClass:this.javaclasses)
           {
               if (javaClass.getClassName()!=null)
               {
                   if (javaClass.getClassName().equals(jc.getClassName()))
                   {
                       return javaClass;
                   }
               }
               else
               {
                   try {
                       throw  new JavaClassObjectNotFoundException("class with NULL classname");
                   } catch (JavaClassObjectNotFoundException e) {
                       e.printStackTrace();
                   }
               }

           }
       }
       else
       {
           try {
               throw  new JavaClassObjectNotFoundException("class with NULL classname");
           } catch (JavaClassObjectNotFoundException e) {
               e.printStackTrace();
           }
       }
       return null;
    }
}
