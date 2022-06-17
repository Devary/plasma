/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import files.IAbstractFile;
import projects.ProjectFile;
import projects.ProjectImpl;
import services.reporting.Report;

import java.util.ArrayList;

public interface IAbstractProcess {
    public ProjectImpl createProject(String basePath);
    public IAbstractFile createAbstractFile();
    ArrayList createObjectFiles(ArrayList<ProjectFile> projectJavaFiles, Report report) throws Exception;
}
