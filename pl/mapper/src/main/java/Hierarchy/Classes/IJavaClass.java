package Hierarchy.Classes;

import files.AbstractFile;
import files.IAbstractFile;

public interface IJavaClass extends IAbstractFile {
    @Override
    String getName();

    @Override
    String getExtension();

    @Override
    AbstractFile refresh();

    @Override
    AbstractFile[] getChildren();

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
