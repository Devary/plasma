/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package hierarchy.Classes;

import hierarchy.Classes.types.JavaField;
import hierarchy.Classes.types.Function;
import hierarchy.persistence.Persistent;
import files.AbstractFile;
import projects.ProjectFile;

import java.util.ArrayList;

public class JavaClass extends ProjectFile implements IJavaClass {

    public String className;
    public ArrayList<Function> functions;
    public ArrayList<JavaClass> implementations;
    public ArrayList<JavaClass> heritances;
    public ArrayList<JavaField> javaFields;
    public Persistent persistent;
    public String classType;
    public String module;
    public String containingPackage;
    public boolean isInnerClass;

    public JavaClass(Persistent persistent) {
        this.persistent = persistent;
    }

    private JavaClass(Builder builder) {
        this.className = builder.className;
        this.functions = builder.functions;
        this.implementations = builder.implementations;
        this.heritances = builder.heritances;
        this.javaFields = builder.javaFields;
        this.persistent = builder.persistent;
        this.classType = builder.classType;
        this.isInnerClass = builder.isInnerClass;
        this.module = builder.module;
        this.containingPackage = builder.containingPackage;

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


    public ArrayList<JavaField> getJavaFields() {
        return javaFields;
    }

    public void setJavaFields(ArrayList<JavaField> javaFields) {
        this.javaFields = javaFields;
    }

    public boolean isInnerClass() {
        return isInnerClass;
    }

    public void setInnerClass(boolean innerClass) {
        isInnerClass = innerClass;
    }

    public String getModule() {
        return module;
    }

    public String getContainingPackage() {
        return containingPackage;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setContainingPackage(String containingPackage) {
        this.containingPackage = containingPackage;
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

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }
    public static final class Builder {
        public String classType;
        public boolean isInnerClass;
        private String className;
        private ArrayList<Function> functions;
        private ArrayList<JavaClass> implementations;
        private ArrayList<JavaClass> heritances;
        private ArrayList<JavaField> javaFields;
        private Persistent persistent;
        public String module;
        public String containingPackage;

        private Builder() {
        }

        public JavaClass build() {
            return new JavaClass(this);
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }
        public Builder classType(String classType) {
            this.classType = classType;
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

        public Builder declarations(ArrayList<JavaField> javaFields) {
            this.javaFields = javaFields;
            return this;
        }
        public Builder isInnerClass(boolean isInnerClass) {
            this.isInnerClass = isInnerClass;
            return this;
        }

        public Builder persistent(Persistent persistent) {
            this.persistent = persistent;
            return this;
        }
        public Builder setModule(String module) {
            this.module = module;
            return this;
        }
        public Builder setContainingPackage(String containingPackage) {
            this.containingPackage = containingPackage;
            return this;
        }
    }
}
