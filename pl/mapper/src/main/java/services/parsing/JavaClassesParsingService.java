/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.parsing;

import Hierarchy.Classes.JavaClass;
import descriptors.ClassDescriptor;
import projects.ProjectFile;

import java.io.*;
import java.util.ArrayList;

public class JavaClassesParsingService {
    JavaClass javaClass = JavaClass.newJavaClass().build();
    ProjectFile file;
    private ArrayList<JavaClass> innerClasses = new ArrayList<>();

    public static final String[] CLASS_TYPES = {"public class", "public interface", "public enum"};

    public JavaClassesParsingService(ProjectFile projectFile) {
        this.file = projectFile;
        String[] typesToParse =adaptParsingRange();
            parseContent(typesToParse);
    }

    private String[] adaptParsingRange() {
        String[] types = {
                ClassDescriptor.PUBLIC+" "+ClassDescriptor.CLASS,
                ClassDescriptor.PUBLIC+" "+ClassDescriptor.INTERFACE,
                ClassDescriptor.PUBLIC+" "+ClassDescriptor.ANNOTATION,
                ClassDescriptor.FINAL+" "+ClassDescriptor.CLASS,
                ClassDescriptor.STATIC+" "+ClassDescriptor.CLASS,
                ClassDescriptor.STRICTFP+" "+ClassDescriptor.CLASS,
        };
        return types;
    }

    private void parseContent(String[] classTypes) {
        File fileToRead = new File(file.getPath());
        int count = 0;
        try (FileReader fileStream = new FileReader(fileToRead);
             BufferedReader bufferedReader = new BufferedReader(fileStream)) {
            StringBuilder header = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                if (!line.startsWith("//"))
                {
                    ArrayList<Integer> classTypeParams = getClassTypeParams(line,classTypes);
                    if (classTypeParams==null)
                    {
                        continue;
                    }
                    int indexClassType = classTypeParams.get(1);
                    int classTypeLength = classTypes[classTypeParams.get(0)].length();
                    if (indexClassType != -1) {
                        System.out.println(header + line);
                        int indexOfOpeningAcco = line.indexOf("{");
                        if (line.contains("//"))
                        {
                            line = removeLineComments(line);
                        }
                        if (lineIsNewInnerClass(line))
                        {
                            delegateClass(line);
                            continue;
                        }

                        header.append(line);
                        if (indexOfOpeningAcco == -1)
                        {
                            header.setLength(header.length() - 1);
                            continue;
                        }
                        System.out.println(header);
                        count++;
                        int indexExtends = header.indexOf("extends");
                        int indexImplements = header.indexOf("implements");
                        ///FIXME: need to find solution public final class ForgivingParameterHandlerStrategyRegistry<HC extends HardCode, STRATEGY> extends AbstractSpringHardCodeRegistry<HardCode, STRATEGY> implements HardCodeStrategyRegistry<HC, STRATEGY>  {
                        if(line.contains("<"))
                        {
                            continue;
                        }
                        if (!assertIsBetweenClassTypeAndImplIndexes(indexClassType,indexImplements,indexExtends)  /* && !isOriginalExtending(line)*/)
                        {
                            indexExtends=-1;
                        }
                        String classname;
                        ///getting class name
                        if (indexExtends == -1 && indexImplements == -1) {
                            classname = header.substring(indexClassType + classTypeLength, indexOfOpeningAcco - 1);

                        } else if (indexImplements != -1 && indexExtends == -1) {
                            classname = header.substring(indexClassType + classTypeLength, indexImplements - 1);
                            getImplementationsFromString(header.toString());
                        } else if (indexExtends != -1 && indexImplements == -1) {
                            classname = header.substring(indexClassType + classTypeLength, indexExtends - 1);
                            getHeritancesFromString(header.toString());
                        } else {
                            classname = header.substring(indexClassType + classTypeLength, indexExtends - 1);
                            getImplementationsFromString(header.toString());
                            getHeritancesFromString(header.toString());
                        }
                        javaClass.setClassName(classname.trim());
                        setClassType(classTypes[classTypeParams.get(0)]);
                    }
                }

            }
            if (count == 0) {
                int index = -1;
                for (int i = 0; i < classTypes.length; i++) {
                    if (classTypes[i].equals(classTypes)) {
                        index = i;
                        break;
                    }
                }
                if (index + 1 < classTypes.length)
                {
                    return;
                    //parseContent(classTypes[index + 1]);
                }
            }

        } catch (FileNotFoundException ex) {
            //exception Handling
        } catch (IOException ex) {
            //exception Handling
        }
    }

    private boolean isOriginalExtending(String line) {
        return !line.contains("<") || (line.indexOf("<")>0 && line.indexOf("extends")>line.indexOf(">"));
    }

    private void delegateClass(String line) {
        //// must be implemented
        String[] classes = line.split("\\{");
        ///Create delegate methode to resolve this class and create it later
        DelegateClassCreation(line);
    }

    private void DelegateClassCreation(String line) {
        /// store delegated classes
    }

    private boolean lineIsNewInnerClass(String line) {
        //// must be implemented
        String[] classTypes = adaptParsingRange();
        ArrayList<Integer> classParams = getClassTypeParams(line,classTypes);
        return classParams!=null && classParams.get(1)!=0;
    }

    private ArrayList<Integer> getClassTypeParams(String line, String[] classTypes) {
        int arrayIdx =-1;
        ArrayList<Integer> indexes = new ArrayList<>();
        for (String classType:classTypes)
        {
            arrayIdx++;
            int idx = line.indexOf(classType);
            if (idx!=-1)
            {
                indexes.add(arrayIdx);
                indexes.add(idx);
            }

        }
        if (!indexes.isEmpty())
        {
            return indexes;
        }
        return null;
    }

    private String removeLineComments(String line) {
        String newLine;
        if (line.indexOf("//")==0)
            return "";
        else
            newLine = line.substring(0,line.indexOf("//")-1);
        if (newLine.contains("//"))
            removeLineComments(newLine);
        return newLine;
    }

    private boolean assertIsBetweenClassTypeAndImplIndexes(int indexClassType, int indexImplements, int indexExtends) {
        return (indexClassType < indexExtends) && (indexExtends < indexImplements);
    }

    private void setClassType(String classType) {
        javaClass.setClassType(classType.split(" ")[1]);
    }

    private void getHeritancesFromString(String line) {
        int indexExtends = line.indexOf("extends");
        int indexImplements = line.indexOf("implements");
        int stopIndex = getStopIndex(line);

        ArrayList<JavaClass> heritances = new ArrayList<>();
        if (indexImplements == -1) {
            String[] classes = line.substring(indexExtends + 8, stopIndex).split(",");
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

    private int getStopIndex(String line) {
        Character space = ' ';
        int indexOfOpeningAcco = line.indexOf("{");
        int stopIndex ;
        System.out.println(line);
        if(space.equals(line.charAt(indexOfOpeningAcco-1)))
        {
            stopIndex =indexOfOpeningAcco - 1;
        }
        else
        {
            stopIndex=indexOfOpeningAcco;
        }
        return stopIndex;
    }

    private JavaClass createJavaClassWithNameTest(String name) {
        return JavaClass.newJavaClass().className(name).build();
    }

    private void getImplementationsFromString(String line) {
        int indexImplements = line.indexOf("implements");
        ArrayList<JavaClass> implementations = new ArrayList<>();
        int stopIndex = getStopIndex(line);

        String[] classes = line.substring(indexImplements + 11, stopIndex).split(",");

        for (String cl : classes) {
            implementations.add(createJavaClassWithNameTest(cl));
        }
        javaClass.setImplementations(implementations);
    }


    public JavaClass getJavaClass() {
        return javaClass;
    }
}
