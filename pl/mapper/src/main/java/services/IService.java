package services;/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

import hierarchy.Classes.JavaClass;
import hierarchy.Classes.types.JavaField;
import hierarchy.persistence.Persistent;
import hierarchy.persistence.types.Link;

import java.util.ArrayList;

public interface IService {
    public ArrayList<Persistent> getPersistents();
    public ArrayList<JavaClass> getJavaClasses();
    public ArrayList<JavaField> getFields();
    public ArrayList<Link> getLinks();
    public ArrayList<com.thoughtworks.qdox.model.JavaClass> getThoughworksJavaClass();
}
