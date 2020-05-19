import Engines.SolifeQueryEngine;
import hierarchy.persistence.types.SolifeQuery;

public class ModelEngine implements IModelEngine{

    @Override
    public boolean store(Object obj) {
        if (obj instanceof SolifeQuery){
            SolifeQueryEngine sqe = new SolifeQueryEngine();
            sqe.create(obj);
        }
        return false;
    }
}
