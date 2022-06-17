package services.processing.validation;

import files.AbstractFile;
import hierarchy.persistence.Persistent;
import projects.ProjectFile;

public class PersistentValidator extends ValidatorImpl {

    @Override
    public boolean validate(Persistent persistent) {
        return isValidName(persistent.getName());
                //&& isValidMapping(persistent.getMappingType()) ;
                //&& isValidSTN(persistent.getShortTableName());
    }

    public boolean isValidName(String name){
        return name != null && name.chars().count()!=0;
    }
    public boolean isValidMapping(String mapping){
        return mapping != null && mapping.chars().count()!=0;
    }
    public boolean isValidSTN(String stn){
        return stn != null && stn.chars().count()!=0;
    }
}
