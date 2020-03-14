import Exceptions.ErrorCodes;
import Exceptions.FileCreationException;
import Hierarchy.Classes.JavaClass;
import Hierarchy.persistence.Persistent;
import Hierarchy.persistence.types.SolifeQuery;
import com.intellij.openapi.application.ApplicationStarter;
import com.intellij.openapi.project.Project;
import files.AbstractFile;
import files.IAbstractFile;
import org.jetbrains.annotations.TestOnly;
import org.junit.Before;
import org.junit.Test;
import projects.IProject;
import projects.ProjectImpl;
import projects.Solife;

import java.io.File;
import java.util.ArrayList;

public class Run {


    public String getCommandName() {
        return null;
    }

    public void premain(String[] strings) {

    }

    public static void main(String[] strings) throws FileCreationException {
        /*IAbstractFile f = Persistent.newPersistent().className("BillingSettings").isPersistent(true).mappingType("hierarchical").name("BillingSettings").tableName("bill_sett").build();
        AbstractFile af = AbstractFile.newAbstractFile().url("ddd").path("rezf").name("zsfrr").isValid(true).extension("persistence").build();
        SolifeQuery sq = SolifeQuery.SolifeQueryBuilder.aSolifeQuery().withClassName("frfzf").build();
        ArrayList<SolifeQuery> solifeQueries = new ArrayList<>();
        solifeQueries.add(sq);

        Persistent per = Persistent.newPersistent().className("ok").queries(new ArrayList<SolifeQuery>(solifeQueries)).build();

        System.out.println(per.toString());
        throw new FileCreationException(ErrorCodes.FILE_CREATION_EXCEPTION);

         */


        ///TODO : create project
        ProjectImpl project = createProject();
        System.out.println(project.getName());
    }

    public static ProjectImpl createProject()
    {
        ProjectImpl project = new ProjectImpl();
        project.setBasePath("C:/SolifePlasma");
        project.setMainDirectory((AbstractFile)createAbstractFile());
        project.setName("SOLIFE");
        return project;
    }
    public static IAbstractFile createAbstractFile()
    {
        return AbstractFile.newAbstractFile().path("C:/SolifePlasma").name("is").build();
    }

}
