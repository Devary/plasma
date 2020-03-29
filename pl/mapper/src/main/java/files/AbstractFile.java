package files;

import Hierarchy.Classes.JavaClass;
import Hierarchy.Classes.types.Declaration;
import Hierarchy.Classes.types.Function;
import Hierarchy.persistence.Persistent;

import java.util.ArrayList;

public class AbstractFile implements IAbstractFile {

    private String url;
    private String path;
    private boolean isDirectory;
    private boolean isValid;
    private AbstractFile parent;
    private ArrayList<AbstractFile> children;
    private String name;
    private String extension;
    private ArrayList<FileCreationException> exceptions;

    private AbstractFile(Builder builder) {
        this.url = builder.url;
        this.path = builder.path;
        this.isDirectory = builder.isDirectory;
        this.isValid = builder.isValid;
        this.parent = builder.parent;
        this.children = builder.children;
        this.name = builder.name;
        this.extension = builder.extension;
    }

    public ArrayList<FileCreationException> getExceptions() {
        return exceptions;
    }

    public void addException(FileCreationException exception) {
        this.exceptions.add(exception);
    }
    public static AbstractFile getInstance() {
        return new AbstractFile();
    }
    public AbstractFile(String path) {
        this.path = path;
    }

    public AbstractFile() {
    }

    public static Builder newAbstractFile() {
        return new Builder();
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

    public ArrayList<AbstractFile> getChildren() {
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

    public void setPath(String path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExtension(String extension) {
        if (checkFileExtentionIntegrity(extension))
        {
            this.extension = extension;
        }
        else
        {
            StringBuilder details = new StringBuilder();
            details.append("adding ")
                    .append(extension)
                    .append(" file ")
                    .append("is not a known extention");
            createException(ErrorCodes.EXTENSION_TYPE_EXCEPTION,details);
            this.extension = null;
        }
        /// TODO : add logger
    }

    private void createException(String exceptionTitle, StringBuilder details) {
        FileCreationException fileCreationException = new FileCreationException(exceptionTitle,details.toString());
        addException(fileCreationException);
    }

    private boolean checkFileExtentionIntegrity(String extension) {
        for (String fileType:FileTypes.ALL_CODES)
        {
            if (fileType.equals("."+extension))
                return true;
        }
        return false;
    }


    public static final class Builder {
        private String url;
        private String path;
        private boolean isDirectory;
        private boolean isValid;
        private AbstractFile parent;
        private ArrayList<AbstractFile> children;
        private String name;
        private String extension;
        private String className;
        private ArrayList<Function> functions;
        private ArrayList<JavaClass> implementations;
        private ArrayList<JavaClass> heritances;
        private ArrayList<Declaration> declarations;
        private Persistent persistent;

        private Builder() {
        }

        public AbstractFile build() {
            return new AbstractFile(this);
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder isDirectory(boolean isDirectory) {
            this.isDirectory = isDirectory;
            return this;
        }

        public Builder isValid(boolean isValid) {
            this.isValid = isValid;
            return this;
        }

        public Builder parent(AbstractFile parent) {
            this.parent = parent;
            return this;
        }

        public Builder children(ArrayList<AbstractFile> children) {
            this.children = children;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder extension(String extension) {
            this.extension = extension;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder functions(ArrayList<Function> functions) {
            this.functions = functions;
            return this;
        }

        public Builder implementations(ArrayList<JavaClass> implementations) {
            this.implementations = implementations;
            return this;
        }

        public Builder heritances(ArrayList<JavaClass> heritances) {
            this.heritances = heritances;
            return this;
        }

        public Builder declarations(ArrayList<Declaration> declarations) {
            this.declarations = declarations;
            return this;
        }

        public Builder persistent(Persistent persistent) {
            this.persistent = persistent;
            return this;
        }
    }
}
