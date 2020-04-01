/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package projects;

import files.AbstractFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;

public interface IProject {
    public void setMainDirectory(AbstractFile mainDirectory);
    public String getName();
    public void setName(@NotNull String name);
    public String getBasePath();
    public void setBasePath(@NotNull String basePath);
    public ArrayList<ProjectFile> getProjectFiles();
    }
