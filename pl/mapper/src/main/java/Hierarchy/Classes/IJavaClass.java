package Hierarchy.Classes;

import files.AbstractFile;
import files.IAbstractFile;

import java.util.ArrayList;

public interface IJavaClass extends IAbstractFile {
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
