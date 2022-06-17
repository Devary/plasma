/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

import hierarchy.Classes.JavaClass;
import hierarchy.Classes.types.JavaField;
import hierarchy.persistence.Persistent;
import hierarchy.persistence.types.Link;

import java.util.ArrayList;

public class IServiceImpl implements IService {

    private ArrayList<Persistent> persistents;
    private ArrayList<JavaClass> javaClasses;
    private ArrayList<JavaField> fields;
    private ArrayList<Link> links;
    private ArrayList<com.thoughtworks.qdox.model.JavaClass> thoughworksJavaClass;

    @Override
    public ArrayList<Persistent> getPersistents() {
        return this.persistents;
    }

    @Override
    public ArrayList<JavaClass> getJavaClasses() {
        return this.javaClasses;
    }

    @Override
    public ArrayList<JavaField> getFields() {
        return this.fields;
    }

    @Override
    public ArrayList<Link> getLinks() {
        return this.links;
    }

    @Override
    public ArrayList<com.thoughtworks.qdox.model.JavaClass> getThoughworksJavaClass() {
        return this.thoughworksJavaClass;
    }


    public void setPersistents(ArrayList<Persistent> persistents) {
        this.persistents = persistents;
    }

    public void setJavaClasses(ArrayList<JavaClass> javaClasses) {
        this.javaClasses = javaClasses;
    }

    public void setFields(ArrayList<JavaField> fields) {
        this.fields = fields;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public void setThoughworksJavaClass(ArrayList<com.thoughtworks.qdox.model.JavaClass> thoughworksJavaClass) {
        this.thoughworksJavaClass = thoughworksJavaClass;
    }

    public void addPersistent(Persistent persistent){
        this.persistents.add(persistent);
    }
    public void addJavaClasses(JavaClass javaClass) {
        this.javaClasses.add(javaClass);
    }

    public void setFields(JavaField field) {
        this.fields.add(field);
    }

    public void setLinks(Link link) {
        this.links.add(link);
    }
}