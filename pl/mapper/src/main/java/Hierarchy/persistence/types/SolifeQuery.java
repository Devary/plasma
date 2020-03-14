package Hierarchy.persistence.types;

public class SolifeQuery  {
    private String className;
    private boolean identity;
    private String name;
    private Class resultType;

    public String getClassName() {
        return className;
    }

    private void setClassName(String className) {
        this.className = className;
    }

    public boolean isIdentity() {
        return identity;
    }

    private void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Class getResultType() {
        return resultType;
    }

    private void setResultType(Class resultType) {
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


    public static final class SolifeQueryBuilder {
        private String className;
        private boolean identity=false;
        private String name;
        private Class resultType;

        private SolifeQueryBuilder() {
        }

        public static SolifeQueryBuilder aSolifeQuery() {
            return new SolifeQueryBuilder();
        }

        public SolifeQueryBuilder withClassName(String className) {
            this.className = className;
            return this;
        }

        public SolifeQueryBuilder withIdentity(boolean identity) {
            this.identity = identity;
            return this;
        }

        public SolifeQueryBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SolifeQueryBuilder withResultType(Class resultType) {
            this.resultType = resultType;
            return this;
        }

        public SolifeQueryBuilder but() {
            return aSolifeQuery().withClassName(className).withIdentity(identity).withName(name).withResultType(resultType);
        }

        public SolifeQuery build() {
            SolifeQuery solifeQuery = new SolifeQuery();
            solifeQuery.setClassName(className);
            solifeQuery.setIdentity(identity);
            solifeQuery.setName(name);
            solifeQuery.setResultType(resultType);
            return solifeQuery;
        }
    }
}
