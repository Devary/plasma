/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package Hierarchy.Classes.types;

import Hierarchy.Classes.JavaClass;

import java.util.ArrayList;

public class Function {
    private String name;
    private JavaClass resultType;
    private ArrayList<JavaClass> inputs;
    private   ArrayList<JavaClass> manipulatedObjects;
    private boolean canReturnNull;
    private boolean isService;
    private boolean isVoid;
    private ArrayList<Function> usersOfThisMethode;

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public JavaClass getResultType() {
        return resultType;
    }

    private void setResultType(JavaClass resultType) {
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

    public static final class FunctionBuilder {
        private String name;
        private JavaClass resultType;
        private ArrayList<JavaClass> inputs;
        private boolean isVoid;
        private  ArrayList<JavaClass> manipulatedObjects;
        private boolean canReturnNull;
        private boolean isService;
        private ArrayList<Function> usersOfThisMethode;

        private FunctionBuilder() {
        }

        private static FunctionBuilder aFunction() {
            return new FunctionBuilder();
        }

        private FunctionBuilder withName(String name) {
            this.name = name;
            return this;
        }

        private FunctionBuilder withResultType(JavaClass resultType) {
            this.resultType = resultType;
            return this;
        }

        private FunctionBuilder withInputs(ArrayList<JavaClass> inputs) {
            this.inputs = inputs;
            return this;
        }

        private FunctionBuilder withIsVoid(boolean isVoid) {
            this.isVoid = isVoid;
            return this;
        }

        private FunctionBuilder withManipulatedObjects(ArrayList<JavaClass> manipulatedObjects) {
            this.manipulatedObjects = manipulatedObjects;
            return this;
        }

        private FunctionBuilder withCanReturnNull(boolean canReturnNull) {
            this.canReturnNull = canReturnNull;
            return this;
        }

        private FunctionBuilder withIsService(boolean isService) {
            this.isService = isService;
            return this;
        }

        private FunctionBuilder withUsersOfThisMethode(ArrayList<Function> usersOfThisMethode) {
            this.usersOfThisMethode = usersOfThisMethode;
            return this;
        }

        public FunctionBuilder but() {
            return aFunction().withName(name).withResultType(resultType).withInputs(inputs).withIsVoid(isVoid).withManipulatedObjects(manipulatedObjects).withCanReturnNull(canReturnNull).withIsService(isService).withUsersOfThisMethode(usersOfThisMethode);
        }

        public Function build() {
            Function function = new Function();
            function.setName(name);
            function.setResultType(resultType);
            function.setInputs(inputs);
            function.setManipulatedObjects(manipulatedObjects);
            function.setCanReturnNull(canReturnNull);
            function.setUsersOfThisMethode(usersOfThisMethode);
            function.isService = this.isService;
            function.isVoid = this.isVoid;
            return function;
        }
    }
}
