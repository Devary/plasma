package files;

import Hierarchy.persistence.Persistent;
import Hierarchy.persistence.types.Field;
import Hierarchy.persistence.types.Links;
import Hierarchy.persistence.types.SolifeQuery;

import java.util.ArrayList;

public class AbstractFile implements IAbstractFile {

    private String url;
    private String path;
    private boolean isDirectory;
    private boolean isValid;
    private AbstractFile parent;
    private AbstractFile[] children;
    private String name;
    private String extension;

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

    public static AbstractFile getInstance() {
        return new AbstractFile();
    }
    public AbstractFile(String path) {
        this.path = path;
    }

    ///TODO : create constructor without params
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


    public static final class Builder {
        private String url;
        private String path;
        private boolean isDirectory;
        private boolean isValid;
        private AbstractFile parent;
        private AbstractFile[] children;
        private String name;
        private String extension;

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

        public Builder children(AbstractFile[] children) {
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
    }
}