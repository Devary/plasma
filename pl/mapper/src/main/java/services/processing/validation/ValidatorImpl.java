package services.processing.validation;

import files.AbstractFile;
import hierarchy.persistence.Persistent;

public abstract class ValidatorImpl implements IValidator {
    @Override
    public boolean validate(AbstractFile obj) {
        return false;
    }

    public abstract boolean validate(Persistent persistent);
}
