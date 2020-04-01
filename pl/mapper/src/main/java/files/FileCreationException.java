/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package files;

public class FileCreationException {

    private String title = "";
    private String details="";

    public FileCreationException(String name,String details) {
        this.title = name;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }
    public String getDetails() {
        return details;
    }
}
