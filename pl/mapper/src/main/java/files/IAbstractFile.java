package files;

public interface IAbstractFile {
    public String getName();
    public String getExtension();
    public AbstractFile refresh();
    public AbstractFile[] getChildren();
    public AbstractFile getParent();
    public boolean isValid();
    public boolean isDirectory();
    public String getPath();
    public String getUrl();
}
