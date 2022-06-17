import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.model.JavaType;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameterizedType;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class TikaTest {

    public static void main(String[] args) {
        TikaTest tt = new TikaTest();
        try {
            tt.process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process() throws IOException {
        parseJavaSourceFile();
    }

    private void parseJavaSourceFile() {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        try {
            JavaSource src = builder.addSource(new FileReader("C:\\sandPlas\\plasma\\pl\\mapper\\src\\main\\java\\MainProcess.java"));
            JavaField jf = src.getClasses().get(0).getFields().get(2);
            hierarchy.Classes.types.JavaField pjf = cloneToPlasmaField(jf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private hierarchy.Classes.types.JavaField cloneToPlasmaField(JavaField javaField) {
        String fname = javaField.getName();
        String ftype = null;
        String collectionType = null;
        boolean isCollection = false;

        List<JavaType> defaultJavaType = ((DefaultJavaParameterizedType) javaField.getType()).getActualTypeArguments();
        if (defaultJavaType.isEmpty()){
            ftype = javaField.getType().getName();
        }else{
            for (JavaType javaType: defaultJavaType){
                ftype = ((DefaultJavaParameterizedType)javaType).getSimpleName();
                collectionType = javaType.getFullyQualifiedName();
            }
            isCollection = true;
        }

        return new hierarchy.Classes.types.JavaField(fname, ftype, isCollection,collectionType);
    }
}
