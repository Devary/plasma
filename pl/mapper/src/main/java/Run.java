import Hierarchy.Classes.JavaClass;
import Hierarchy.persistence.Persistent;
import com.intellij.openapi.application.ApplicationStarter;
import files.IAbstractFile;

public class Run {


    public String getCommandName() {
        return null;
    }

    public void premain(String[] strings) {

    }

    public static void main(String[] strings) {
        IAbstractFile f = Persistent.newPersistent().className("BillingSettings").isPersistent(true).mappingType("hierarchical").name("BillingSettings").tableName("bill_sett").build();
        System.out.println(f.toString());
    }
}
