/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package hierarchy.Classes.types;

public class JavaField {
    private String name ;
    private String type ;

    public JavaField(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }

}
