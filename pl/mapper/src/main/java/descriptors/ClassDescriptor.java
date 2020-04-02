package descriptors;

public class ClassDescriptor {

    public static final String PUBLIC = "public";
    public static final String PRIVATE = "private";
    public static final String EMPTY = "";
    public static final String DEFAULT = "default";
    public static final String PROTECTED = "protected";
    public static final String ABSTRACT = "abstract";
    public static final String STATIC = "static";
    public static final String FINAL = "final";
    public static final String STRICTFP = "strictfp";
    public static final String ENUM = "enum";
    public static final String CLASS = "class";
    public static final String INTERFACE = "interface";
    public static final String ANNOTATION = "@interface";
    public static final String LINE_COMMENT = "//";
    public static final String START_BLOCK_COMMENT = "/*";
    public static final String END_BLOCK_COMMENT = "*/";


    public static final String[] ACCESSORS = {
            PUBLIC,
            PRIVATE,
            EMPTY,
            DEFAULT,
            PROTECTED,
            ABSTRACT,
            STATIC,
            FINAL,
            STRICTFP
    };

    public static final String[] CLASS_TYPES = {
            ENUM,
            CLASS,
            INTERFACE,
            ANNOTATION,
    };


    public static final String[] COMMENT_TYPES = {
            LINE_COMMENT,
            START_BLOCK_COMMENT,
            END_BLOCK_COMMENT,
    };
    
}

