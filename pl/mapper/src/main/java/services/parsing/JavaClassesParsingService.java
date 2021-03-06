/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.parsing;

import Exceptions.ClassNameNotFoundException;
import Exceptions.ErrorCodes;
import Exceptions.ParsingException;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import hierarchy.Classes.JavaClass;
import descriptors.ClassDescriptor;
import hierarchy.Classes.types.Function;
import projects.ProjectFile;
import services.reporting.Report;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JavaClassesParsingService {
    JavaClass javaClass = JavaClass.newJavaClass().build();
    ProjectFile file;
    StringBuilder content = new StringBuilder();
    private ArrayList<JavaClass> innerClasses = new ArrayList<>();
    private final String plasmaGeneratedClassesDir =System.getProperty("user.dir")+"/mapper/src/main/java/plasma_generated_classes/";

    public static final String[] CLASS_TYPES = {"public class", "public interface", "public enum"};

    public JavaClassesParsingService(ProjectFile projectFile, Report report) throws ClassNameNotFoundException {
        this.file = projectFile;
        String[] typesToParse =adaptParsingRange();
            parseContent(typesToParse);
    }

    private String[] adaptParsingRange() {
        String[] types = {
                ClassDescriptor.PUBLIC+" "+ClassDescriptor.CLASS,
                ClassDescriptor.PUBLIC+" "+ClassDescriptor.INTERFACE,
                ClassDescriptor.PUBLIC+" "+ClassDescriptor.ANNOTATION,
                ClassDescriptor.PUBLIC+" "+ClassDescriptor.FINAL+" "+ClassDescriptor.CLASS,
                ClassDescriptor.FINAL+" "+ClassDescriptor.CLASS,
                ClassDescriptor.STATIC+" "+ClassDescriptor.CLASS,
                ClassDescriptor.STRICTFP+" "+ClassDescriptor.CLASS,
        };
        return types;
    }

    private void parseContent(String[] classTypes) throws ClassNameNotFoundException {
        File fileToRead = new File(file.getPath());
        appendContent(fileToRead.getPath());
        int count = 0;
        try (FileReader fileStream = new FileReader(fileToRead);
             BufferedReader bufferedReader = new BufferedReader(fileStream)) {
            StringBuilder header = new StringBuilder();
            String line = null;
            String classname = null;
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

                        if(line.contains("<"))
                        {
                            header=cleanHeaderFromDiamond(header);
                        }
                        int indexExtends = header.indexOf("extends");
                        int indexImplements = header.indexOf("implements");
                        if (indexImplements != -1 &&!assertIsBetweenIndexes(indexClassType,indexImplements,indexExtends)  /* && !isOriginalExtending(line)*/)
                        {
                            indexExtends=-1;
                        }
                        ///getting class name
                        indexOfOpeningAcco = header.indexOf("{");
                        indexExtends = header.indexOf("extends");
                        indexImplements = header.indexOf("implements");
                        if (indexExtends == -1 && indexImplements == -1) {
                            int nbsp = nbspacesbeforeOpeningAcco(header,indexOfOpeningAcco);
                            classname = header.substring(indexClassType + classTypeLength, indexOfOpeningAcco - nbsp);

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
            //createJavaCloneFile();
            parsePackage();
            parseModule();
            if (classname!= null)
            {
                parseBody(classname);
            }
            else {
                throw new ClassNameNotFoundException(ErrorCodes.FILE_NAME_NOT_FOUND);
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

    private void parseModule() {
        ///TODO:
    }

    private void parsePackage() throws IOException {
        String line = null;
        BufferedReader bufferedReader = new BufferedReader(new StringReader(content.toString()));
            while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("package")) {
                    javaClass.setContainingPackage(line.substring(7,line.lastIndexOf(";")));
            }
        }
    }

    private int nbspacesbeforeOpeningAcco(StringBuilder header, int indexOfOpeningAcco) {
        int nb = 0;
        int i= indexOfOpeningAcco-1;
        StringBuilder sb = new StringBuilder();
        if (header.toString().lastIndexOf(" ") == i)
        {
            nb++;
            sb.append(header.toString().substring(0,header.toString().lastIndexOf(" "))).append("{");
            nb+=nbspacesbeforeOpeningAcco(sb,indexOfOpeningAcco);
        }
        return nb;
    }

    public void parseBody(String className)
    {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSource(new StringReader(content.toString()));
        com.thoughtworks.qdox.model.JavaClass cls = builder.getClasses().iterator().next();
        //Field
        parseFields(cls);
        //Methods
        parseMethods(cls);
    }

    private void parseMethods(com.thoughtworks.qdox.model.JavaClass cls) {
        List<JavaMethod> methods = cls.getMethods();
        ArrayList<Function> javaMethods = new ArrayList<>();
        for (JavaMethod jm:methods)
        {
            javaMethods.add(Function.newFunction()
                    .name(jm.getName())
                    /// TODO: update resultType with existing JAVACLASS in the process
                    .resultType(null)
                    .build()
            );
        }
        javaClass.setFunctions(javaMethods);
    }

    private void parseFields(com.thoughtworks.qdox.model.JavaClass cls) {
        List<JavaField> fields = cls.getFields();
        ArrayList<hierarchy.Classes.types.JavaField> javaFields = new ArrayList<>();
        hierarchy.Classes.types.JavaField javaField = null;
        for (JavaField field:fields)
        {
            String fname = null;
            String fType= null;
            try {
                fname = field.getName();
                fType = field.getType().getName();
                javaField = new hierarchy.Classes.types.JavaField(fname, fType);
                javaFields.add(javaField);
            } catch (Throwable t){
                ////nothing
                t.printStackTrace();
            }
            if (fname == null
            || fType == null){
                ///todo::

            }

        }
        javaClass.setJavaFields(javaFields);
    }

    private void createJavaCloneFile() throws IOException {
        String absPath= plasmaGeneratedClassesDir+javaClass.getClassName()+".java";
        File newFile = new File(absPath);
        if (newFile.createNewFile())
        {
            FileOutputStream fos = new FileOutputStream(absPath);
            fos.write(content.toString().getBytes());
            fos.flush();
            fos.close();
            System.out.println("::::CLONE CREATED::::");
        }
    }

    private void appendContent(String filePath) {
        /// TODO : needs optimization , file reading twice
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.ISO_8859_1))
        {
            stream.forEach(s -> content.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private StringBuilder cleanHeaderFromDiamond(StringBuilder header) {
        StringBuilder newHeader = new StringBuilder();
        String line = header.toString();
        line = line.replaceAll("<(.*?)>","");
        newHeader.append(line);
        return newHeader;
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

    private boolean assertIsBetweenIndexes(int leftSide, int rightSide, int target) {
        return (leftSide < target) && (target < rightSide);
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
