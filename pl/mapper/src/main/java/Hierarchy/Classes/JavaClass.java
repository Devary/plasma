package Hierarchy.Classes;

import Hierarchy.Classes.types.Declaration;
import Hierarchy.Classes.types.Function;
import Hierarchy.persistence.Persistent;
import files.AbstractFile;

import java.util.ArrayList;

public class JavaClass implements IJavaClass {

    public String className;
    public ArrayList<Function> functions;
    public ArrayList<JavaClass> implementations;
    public ArrayList<JavaClass> heritances;
    public ArrayList<Declaration> declarations;
    public Persistent persistent;

    private JavaClass(Builder builder) {
        this.className = builder.className;
        this.functions = builder.functions;
        this.implementations = builder.implementations;
        this.heritances = builder.heritances;
        this.declarations = builder.declarations;
        this.persistent = builder.persistent;
    }

    public static Builder newJavaClass() {
        return new Builder();
    }


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

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getExtension() {
        return null;
    }

    @Override
    public AbstractFile refresh() {
        return null;
    }

    @Override
    public ArrayList<AbstractFile> getChildren() {
        return null;
    }

    @Override
    public AbstractFile getParent() {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getUrl() {
        return null;
    }

    public static final class Builder {
        private String className;
        private ArrayList<Function> functions;
        private ArrayList<JavaClass> implementations;
        private ArrayList<JavaClass> heritances;
        private ArrayList<Declaration> declarations;
        private Persistent persistent;

        private Builder() {
        }

        public JavaClass build() {
            return new JavaClass(this);
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder functions(ArrayList<Function> functions) {
            this.functions = functions;
            return this;
        }

        public Builder implementations(ArrayList<JavaClass> implementations) {
            this.implementations = implementations;
            return this;
        }

        public Builder heritances(ArrayList<JavaClass> heritances) {
            this.heritances = heritances;
            return this;
        }

        public Builder declarations(ArrayList<Declaration> declarations) {
            this.declarations = declarations;
            return this;
        }

        public Builder persistent(Persistent persistent) {
            this.persistent = persistent;
            return this;
        }
    }
}
