package projects;

import files.AbstractFile;

import javax.validation.constraints.NotNull;

public interface IProject {
    public void setMainDirectory(AbstractFile mainDirectory);
    public String getName();
    public void setName(@NotNull String name);
    public String getBasePath();
    public void setBasePath(@NotNull String basePath);

    }
