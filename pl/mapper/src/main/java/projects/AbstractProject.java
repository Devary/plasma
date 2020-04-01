/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package projects;

import files.AbstractFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class AbstractProject implements IProject {

    public String name;
    public String basePath;
    public AbstractFile mainDirectory;

    public AbstractFile getMainDirectory() {
        return mainDirectory;
    }

    public void setMainDirectory(AbstractFile mainDirectory) {
        this.mainDirectory = mainDirectory;
    }
    public AbstractProject(@NotNull String name, @NotNull String basePath) {
        this.name = name;
        this.basePath = basePath;
    }

    public AbstractProject() {
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(@NotNull String basePath) {
        this.basePath = basePath;
    }

    @Override
    public ArrayList<ProjectFile> getProjectFiles() {
        return null;
    }


}
