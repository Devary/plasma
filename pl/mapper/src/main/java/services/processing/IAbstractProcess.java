/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import Hierarchy.persistence.Persistent;
import files.IAbstractFile;
import projects.ProjectFile;
import projects.ProjectImpl;

import java.util.ArrayList;

public interface IAbstractProcess {
    public ProjectImpl createProject();
    public IAbstractFile createAbstractFile();
    ArrayList createObjectFiles(ArrayList<ProjectFile> projectJavaFiles);
}
