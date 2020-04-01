/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package projects;


import files.AbstractFile;

import javax.validation.constraints.NotNull;

public class Solife extends AbstractProject{

    @Override
    public AbstractFile getMainDirectory() {
        return super.getMainDirectory();
    }

    public Solife(@NotNull String name, @NotNull String basePath) {
        super(name, basePath);
    }

    public Solife() {
        super();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(@NotNull String name) {
        super.setName(name);
    }

    @Override
    public String getBasePath() {
        return super.getBasePath();
    }

    @Override
    public void setBasePath(@NotNull String basePath) {
        super.setBasePath(basePath);
    }
}