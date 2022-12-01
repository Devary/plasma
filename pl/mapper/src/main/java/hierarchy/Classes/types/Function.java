/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package hierarchy.Classes.types;

import hierarchy.Classes.JavaClass;

import java.util.ArrayList;

public class Function {
    private String name;
    private String resultType;
    private ArrayList<JavaClass> inputs;
    private   ArrayList<JavaClass> manipulatedObjects;
    private boolean canReturnNull;
    private boolean isService;
    private boolean isVoid;
    private ArrayList<Function> usersOfThisMethode;
    private int class_id;
    private int id;

    private Function(Builder builder) {
        this.name = builder.name;
        this.resultType = builder.resultType;
        this.inputs = builder.inputs;
        this.manipulatedObjects = builder.manipulatedObjects;
        this.canReturnNull = builder.canReturnNull;
        this.isService = builder.isService;
        this.isVoid = builder.isVoid;
        this.usersOfThisMethode = builder.usersOfThisMethode;
        this.class_id = builder.class_id;
        this.id = builder.id;
    }

    public static Builder newFunction() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getResultType() {
        return resultType;
    }

    private void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public ArrayList<JavaClass> getInputs() {
        return inputs;
    }

    private void setInputs(ArrayList<JavaClass> inputs) {
        this.inputs = inputs;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public void setVoid(boolean aVoid) {
        isVoid = aVoid;
    }

    public ArrayList<JavaClass> getManipulatedObjects() {
        return manipulatedObjects;
    }

    private void setManipulatedObjects(ArrayList<JavaClass> manipulatedObjects) {
        this.manipulatedObjects = manipulatedObjects;
    }

    public boolean isCanReturnNull() {
        return canReturnNull;
    }

    private void setCanReturnNull(boolean canReturnNull) {
        this.canReturnNull = canReturnNull;
    }

    public boolean isService() {
        return isService;
    }

    public void setService(boolean service) {
        isService = service;
    }

    public ArrayList<Function> getUsersOfThisMethode() {
        return usersOfThisMethode;
    }

    private void setUsersOfThisMethode(ArrayList<Function> usersOfThisMethode) {
        this.usersOfThisMethode = usersOfThisMethode;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static final class Builder {
        private String name;
        private String resultType;
        private ArrayList<JavaClass> inputs;
        private ArrayList<JavaClass> manipulatedObjects;
        private boolean canReturnNull;
        private boolean isService;
        private boolean isVoid;
        private ArrayList<Function> usersOfThisMethode;
        private int class_id;
        private int id;

        private Builder() {
        }

        public Function build() {
            return new Function(this);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder resultType(String resultType) {
            this.resultType = resultType;
            return this;
        }

        public Builder inputs(ArrayList<JavaClass> inputs) {
            this.inputs = inputs;
            return this;
        }

        public Builder manipulatedObjects(ArrayList<JavaClass> manipulatedObjects) {
            this.manipulatedObjects = manipulatedObjects;
            return this;
        }

        public Builder canReturnNull(boolean canReturnNull) {
            this.canReturnNull = canReturnNull;
            return this;
        }

        public Builder isService(boolean isService) {
            this.isService = isService;
            return this;
        }

        public Builder isVoid(boolean isVoid) {
            this.isVoid = isVoid;
            return this;
        }
        public Builder classId(int class_id) {
            this.class_id = class_id;
            return this;
        }
        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder usersOfThisMethode(ArrayList<Function> usersOfThisMethode) {
            this.usersOfThisMethode = usersOfThisMethode;
            return this;
        }
    }
}
