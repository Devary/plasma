package Hierarchy.persistence.types;

public class SolifeQuery  {
    private String className;
    private boolean identity;
    private String name;
    private String resultType;

    private SolifeQuery(Builder builder) {
        this.className = builder.className;
        this.identity = builder.identity;
        this.name = builder.name;
        this.resultType = builder.resultType;
    }

    public static Builder newSolifeQuery() {
        return new Builder();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isIdentity() {
        return identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResultType() {
        return resultType;
    }

    private void setResultType(String resultType) {
        this.resultType = resultType;
    }

    @Override
    public String toString() {
        return "SolifeQuery{" +
                "className='" + className + '\'' +
                ", identity=" + identity +
                ", name='" + name + '\'' +
                ", resultType=" + resultType +
                '}';
    }


    public static final class Builder {
        private String className;
        private boolean identity;
        private String name;
        private String resultType;

        private Builder() {
        }

        public SolifeQuery build() {
            return new SolifeQuery(this);
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder identity(boolean identity) {
            this.identity = identity;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder resultType(String resultType) {
            this.resultType = resultType;
            return this;
        }
    }
}
