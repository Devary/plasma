package Filters;



public class PersistenceFilesFilter implements VirtualFileFilter {

    public boolean accept(VirtualFileFilter virtualFile) {
        return "Hierarchy/persistence".equals(virtualFile.getExtension());
    }

    public String getExtension() {
        return null;
    }
}
