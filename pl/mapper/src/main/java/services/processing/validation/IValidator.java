package services.processing.validation;

import files.AbstractFile;
import projects.ProjectFile;

public interface IValidator {
    public boolean validate(AbstractFile obj);
}
