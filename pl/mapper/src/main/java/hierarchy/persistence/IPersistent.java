/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package hierarchy.persistence;

import files.AbstractFile;
import files.IAbstractFile;

import java.util.ArrayList;

public interface IPersistent extends IAbstractFile {

    @Override
    String getName();

    @Override
    String getExtension();

    @Override
    AbstractFile refresh();

    @Override
    ArrayList<AbstractFile> getChildren();

    @Override
    AbstractFile getParent();

    @Override
    boolean isValid();

    @Override
    boolean isDirectory();

    @Override
    String getPath();

    @Override
    String getUrl();
}
