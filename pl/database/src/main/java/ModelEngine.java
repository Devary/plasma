import Engines.Engine;
import Engines.custom_engines.*;
import hierarchy.Classes.JavaClass;
import hierarchy.persistence.Persistent;
import hierarchy.persistence.types.Code;
import hierarchy.persistence.types.Link;
import hierarchy.persistence.types.SolifeQuery;

import java.sql.SQLException;

public class ModelEngine implements IModelEngine{

    @Override
    public boolean store(Object obj) throws SQLException {
        if (obj instanceof SolifeQuery){
            SolifeQueryEngine sqe = new SolifeQueryEngine();
            sqe.create(obj);
        }else if (obj instanceof Link){
            LinksEngine linksEngine = new LinksEngine();
            linksEngine.create(obj);
        }else if (obj instanceof Persistent){
            PersistencesEngine linksEngine = new PersistencesEngine();
            linksEngine.create(obj);
        }else if (obj instanceof Code){
            CodesEngine codesEngine = new CodesEngine();
            codesEngine.create(obj);
        }else if (obj instanceof JavaClass){
            ClassEngine classEngine = new ClassEngine();
            classEngine.create(obj);
        }
        return false;
    }
}
