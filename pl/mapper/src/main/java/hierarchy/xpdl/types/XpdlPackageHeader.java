package hierarchy.xpdl.types;

public class XpdlPackageHeader {
    String xpdlVersion;
    String vendor;
    String created;
    String modificationDate;
    String description;
    String documentation;

    private XpdlPackageHeader(Builder builder) {
        this.xpdlVersion = builder.xpdlVersion;
        this.vendor = builder.vendor;
        this.created = builder.created;
        this.modificationDate = builder.modificationDate;
        this.description = builder.description;
        this.documentation = builder.documentation;
    }

    public static Builder newXpdlPackageHeader() {
        return new Builder();
    }

    public static final class Builder {
        private String xpdlVersion;
        private String vendor;
        private String created;
        private String modificationDate;
        private String description;
        private String documentation;

        private Builder() {
        }

        public XpdlPackageHeader build() {
            return new XpdlPackageHeader(this);
        }

        public Builder xpdlVersion(String xpdlVersion) {
            this.xpdlVersion = xpdlVersion;
            return this;
        }

        public Builder vendor(String vendor) {
            this.vendor = vendor;
            return this;
        }

        public Builder created(String created) {
            this.created = created;
            return this;
        }

        public Builder modificationDate(String modificationDate) {
            this.modificationDate = modificationDate;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder documentation(String documentation) {
            this.documentation = documentation;
            return this;
        }
    }
}
