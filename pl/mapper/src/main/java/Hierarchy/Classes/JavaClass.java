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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ArrayList<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(ArrayList<Function> functions) {
        this.functions = functions;
    }

    public ArrayList<JavaClass> getImplementations() {
        return implementations;
    }

    public void setImplementations(ArrayList<JavaClass> implementations) {
        this.implementations = implementations;
    }

    public ArrayList<JavaClass> getHeritances() {
        return heritances;
    }

    public void setHeritances(ArrayList<JavaClass> heritances) {
        this.heritances = heritances;
    }

    public Persistent getPersistent() {
        return persistent;
    }

    public void setPersistent(Persistent persistent) {
        this.persistent = persistent;
    }

}
