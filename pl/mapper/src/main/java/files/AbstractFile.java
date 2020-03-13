package files;

public class AbstractFile implements IAbstractFile {

    private String url;
    private String path;
    private boolean isDirectory;
    private boolean isValid;
    private AbstractFile parent;
    private AbstractFile[] children;
    private String name;
    private String extension;

    public static AbstractFile getInstance() {
        return new AbstractFile();
    }
    public AbstractFile(String path) {
        this.path = path;
    }

    ///TODO : create constructor without params
    public AbstractFile() {
    }

    public String getUrl() {
        return this.url;
    }

    public String getPath() {
        return this.path;
    }

    public boolean isDirectory() {
        return this.isDirectory;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public AbstractFile getParent() {
        return this.parent;
    }

    public AbstractFile[] getChildren() {
        return this.children;
    }

    public AbstractFile refresh() {
        //TODO: return this file but updated
            return new AbstractFile();
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }
}
