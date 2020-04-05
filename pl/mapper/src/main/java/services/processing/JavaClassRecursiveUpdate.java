/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import Hierarchy.Classes.JavaClass;
import Hierarchy.persistence.Persistent;
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
    private ArrayList<Persistent> persistences;

    public JavaClassRecursiveUpdate(ArrayList<JavaClass> javaclasses,ArrayList<Persistent> persistences) {
        this.javaclasses = javaclasses;
        this.persistences = persistences;
        update();
    }

    private void update()
     {
         System.out.println("processing update...");
         for (JavaClass javaClass:javaclasses)
         {
             updateImpl(javaClass);
             updateExt(javaClass);
             updatePersistent(javaClass);
             System.out.println("Class: "+ javaClass.getClassName()+" Has been updated");
         }
         System.out.println("UPDATE DONE !");
     }

    private void updatePersistent(JavaClass javaClass) {
        for (Persistent persistent : persistences) {
            Persistent found = find(persistent,javaClass);

            if (found != null) {
                javaClass.setPersistent(persistent);
                break;
            }
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
    private Persistent find(Persistent persistent,JavaClass javaClass) {
        if (persistent.getClassName()!=null)
        {
            for (Persistent pers:this.persistences)
            {
                if (pers.getClassName()!=null)
                {
                    if (pers.getClassName().equals(javaClass.getClassName()))
                    {
                        return pers;
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
