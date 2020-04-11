/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import hierarchy.Classes.JavaClass;
import hierarchy.persistence.Persistent;

import java.util.ArrayList;
import java.util.Properties;

public class JavaClassRecursiveUpdate {

    private final ArrayList<Properties> properties;
    private ArrayList<JavaClass> javaclasses;
    private ArrayList<Persistent> persistences;

    public JavaClassRecursiveUpdate(ArrayList<JavaClass> javaclasses, ArrayList<Persistent> persistences, ArrayList<Properties> properties) {
        this.javaclasses = javaclasses;
        this.persistences = persistences;
        this.properties = properties;
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

    private void updateCID(JavaClass javaClass) {
        for (Properties property : properties) {
            Properties found = find(property,javaClass);

            //TODO : complete impl
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

    private Properties find(Properties properties,JavaClass javaClass) {
        if (javaClass.getClassName()!=null)
        {
            for (Properties props:this.properties)
            {

                //TODO: find class name in a loaded properties File
                if (props.keys()!=null)
                {
                    if (props.get(1).equals(javaClass.getClassName()))
                    {
                        return props;
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
