/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package Filters;



public class PersistenceFilesFilter implements VirtualFileFilter {

    public boolean accept(VirtualFileFilter virtualFile) {
        return "persistence".equals(virtualFile.getExtension());
    }

    public String getExtension() {
        return null;
    }
}
