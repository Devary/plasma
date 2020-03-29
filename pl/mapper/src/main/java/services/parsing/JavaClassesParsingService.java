package services.parsing;

import Hierarchy.Classes.JavaClass;
import Hierarchy.persistence.Persistent;
import projects.ProjectFile;
import projects.ProjectImpl;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;

public class JavaClassesParsingService {
    private final ProjectImpl project;
    JavaClass javaClass = JavaClass.newJavaClass().build();
    ProjectFile file;

    public JavaClassesParsingService(ProjectImpl project,ProjectFile projectFile) {
        this.project = project;
        this.file = projectFile;
        try {
            parseContent(projectFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseContent(ProjectFile projectFile) throws IOException {
        File fileToRead = new File(file.getPath());

        try( FileReader fileStream = new FileReader( fileToRead );
             BufferedReader bufferedReader = new BufferedReader( fileStream ) ) {

            String line = null;

            while( (line = bufferedReader.readLine()) != null ) {
                int indexPublicClass = line.indexOf("public class");
                if (indexPublicClass != -1)
                {
                    int indexExtends = line.indexOf("extends");
                    int indexImplements = line.indexOf("implements");
                    int indexOfOpeningAcco = line.indexOf("{");
                    String classname;
                    ///getting class name
                    if (indexExtends == -1 && indexImplements==-1)
                    {
                         classname = line.substring(indexPublicClass+13,indexOfOpeningAcco-1);

                    }
                    else if (indexImplements!=-1 && indexExtends == -1 )
                    {
                         classname = line.substring(indexPublicClass+13,indexImplements-1);
                         getImplementationsFromString(line);
                    }
                    else if (indexExtends != -1 && indexImplements==-1)
                    {
                         classname = line.substring(indexPublicClass+13,indexExtends-1);
                         getHeritancesFromString(line);
                    }
                    else {
                         classname = line.substring(indexPublicClass+13,indexExtends-1);
                        getImplementationsFromString(line);
                        getHeritancesFromString(line);
                    }
                    javaClass.setClassName(classname);
                }

            }

        } catch ( FileNotFoundException ex ) {
            //exception Handling
        } catch ( IOException ex ) {
            //exception Handling
        }
    }

    private void getHeritancesFromString(String line) {
        int indexExtends = line.indexOf("extends");
        int indexImplements = line.indexOf("implements");
        int indexOfOpeningAcco = line.indexOf("{");

        ArrayList<JavaClass> heritances = new ArrayList<>();
        if (indexImplements==-1)
        {
            String[] classes = line.substring(indexExtends+8,indexOfOpeningAcco-1).split(",");
            for (String cl:classes)
            {
                heritances.add(createJavaClassWithNameTest(cl));
            }
        }
        else
        {
            String[] classes = line.substring(indexExtends+8,indexImplements-1).split(",");
            for (String cl:classes)
            {
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
        for (String cl:classes)
        {
            implementations.add(createJavaClassWithNameTest(cl));
        }
        javaClass.setImplementations(implementations);
    }

    public JavaClass buildJavaClassObjectFromFile()
    {
        javaClass = JavaClass.newJavaClass()
                /*.className(getClassName())
                .declarations(getDeclarations())
                //.functions(getFunctions())
                .heritances(getHeritances())
                .persistent(getPersistent())
                .implementations(getImplementations())*/
                .build();
        return javaClass;
    }

    public Class loadContent(ProjectFile projectFile, String classPath)
    {
        String filePathAndName = project.getBasePath()+"/created_classes/"+javaClass.getName();
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePathAndName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{

            File file = new File(projectFile.getProject().getBasePath());

            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};

            ClassLoader cl = new URLClassLoader(urls);
            Class cls = cl.loadClass(classPath);
            File newFile = new File(filePathAndName);
            ProtectionDomain pDomain = cls.getProtectionDomain();
            CodeSource cSource = pDomain.getCodeSource();
            if (newFile.createNewFile())
                if (writer != null) {
                    writer.write(cSource.toString());
                    writer.close();
                }
            return newFile.getClass();

            //ProtectionDomain pDomain = cls.getProtectionDomain();
            //CodeSource cSource = pDomain.getCodeSource();
            //URL urlfrom = cSource.getLocation();
            //System.out.println(urlfrom.getFile());

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public JavaClass getJavaClass() {
        return javaClass;
    }
}
