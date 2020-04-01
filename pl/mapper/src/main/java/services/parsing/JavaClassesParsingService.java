/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.parsing;

import Hierarchy.Classes.JavaClass;
import projects.ProjectFile;
import projects.ProjectImpl;
import java.io.*;
import java.util.ArrayList;

public class JavaClassesParsingService {
    JavaClass javaClass = JavaClass.newJavaClass().build();
    ProjectFile file;
    public static final String[] CLASS_TYPES = {"public class", "public interface", "public enum"};

    public JavaClassesParsingService(ProjectFile projectFile) {
        this.file = projectFile;
        parseContent(CLASS_TYPES[0]);
    }

    private void parseContent(String classType) {
        File fileToRead = new File(file.getPath());
        int count = 0;
        try (FileReader fileStream = new FileReader(fileToRead);
             BufferedReader bufferedReader = new BufferedReader(fileStream)) {

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                int indexClassType = line.indexOf(classType);
                int classTypeLength = classType.length();
                if (indexClassType != -1) {
                    count++;
                    int indexExtends = line.indexOf("extends");
                    int indexImplements = line.indexOf("implements");
                    int indexOfOpeningAcco = line.indexOf("{");
                    String classname;
                    ///getting class name
                    if (indexExtends == -1 && indexImplements == -1) {
                        classname = line.substring(indexClassType + classTypeLength, indexOfOpeningAcco - 1);

                    } else if (indexImplements != -1 && indexExtends == -1) {
                        classname = line.substring(indexClassType + classTypeLength, indexImplements - 1);
                        getImplementationsFromString(line);
                    } else if (indexExtends != -1 && indexImplements == -1) {
                        classname = line.substring(indexClassType + classTypeLength, indexExtends - 1);
                        getHeritancesFromString(line);
                    } else {
                        classname = line.substring(indexClassType + classTypeLength, indexExtends - 1);
                        getImplementationsFromString(line);
                        getHeritancesFromString(line);
                    }
                    javaClass.setClassName(classname.trim());
                    setClassType(classType);
                }

            }
            if (count == 0) {
                int index = -1;
                for (int i = 0; i < CLASS_TYPES.length; i++) {
                    if (CLASS_TYPES[i].equals(classType)) {
                        index = i;
                        break;
                    }
                }
                if (index + 1 < CLASS_TYPES.length)
                    parseContent(CLASS_TYPES[index + 1]);
            }

        } catch (FileNotFoundException ex) {
            //exception Handling
        } catch (IOException ex) {
            //exception Handling
        }
    }

    private void setClassType(String classType) {
        javaClass.setClassType(classType.split(" ")[1]);
    }

    private void getHeritancesFromString(String line) {
        int indexExtends = line.indexOf("extends");
        int indexImplements = line.indexOf("implements");
        int indexOfOpeningAcco = line.indexOf("{");

        ArrayList<JavaClass> heritances = new ArrayList<>();
        if (indexImplements == -1) {
            String[] classes = line.substring(indexExtends + 8, indexOfOpeningAcco - 1).split(",");
            for (String cl : classes) {
                heritances.add(createJavaClassWithNameTest(cl));
            }
        } else {
            String[] classes = line.substring(indexExtends + 8, indexImplements - 1).split(",");
            for (String cl : classes) {
                heritances.add(createJavaClassWithNameTest(cl));
            }
        }
        javaClass.setHeritances(heritances);
    }

    private JavaClass createJavaClassWithNameTest(String name) {
        return JavaClass.newJavaClass().className(name).build();
    }

    private void getImplementationsFromString(String line) {
        int indexImplements = line.indexOf("implements");
        int indexOfOpeningAcco = line.indexOf("{");

        ArrayList<JavaClass> implementations = new ArrayList<>();
        String[] classes = line.substring(indexImplements + 11, indexOfOpeningAcco - 1).split(",");
        for (String cl : classes) {
            implementations.add(createJavaClassWithNameTest(cl));
        }
        javaClass.setImplementations(implementations);
    }


    public JavaClass getJavaClass() {
        return javaClass;
    }
}
