package Hierarchy.Classes;

import Hierarchy.Classes.types.Function;
import Hierarchy.persistence.Persistent;
import files.AbstractFile;

import java.util.ArrayList;

public class JavaClass extends AbstractFile implements IJavaClass {

    public String className;
    public ArrayList<Function> functions;
    public ArrayList<JavaClass> implementations;
    public ArrayList<JavaClass> heritances;
    public Persistent persistent;


}
