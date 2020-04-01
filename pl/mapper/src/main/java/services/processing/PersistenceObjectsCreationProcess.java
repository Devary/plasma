/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import Hierarchy.persistence.Persistent;
import files.IAbstractFile;
import projects.ProjectFile;
import projects.ProjectImpl;
import services.parsing.ParsingService;

import java.util.ArrayList;
import java.util.Collection;

public class PersistenceObjectsCreationProcess implements IAbstractProcess {

    private Collection<Persistent> persistents ;

    public Collection<Persistent> getPersistents() {
        return persistents;
    }

    private void setPersistents(Collection<Persistent> persistents) {
        this.persistents = persistents;
    }

    @Override
    public ProjectImpl createProject() {
        return null;
    }

    @Override
    public IAbstractFile createAbstractFile() {
        return null;
    }

    @Override
    public ArrayList<Persistent> createObjectFiles(ArrayList<ProjectFile> projectPersistenceFiles) {
        System.out.println("Processing Object creation");
        /*try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        ArrayList<Persistent> persistents = new ArrayList<>();
        for (ProjectFile projectFile:projectPersistenceFiles)
        {

            ParsingService ps = new ParsingService(projectFile);
            Persistent persistent = ps.buildPersistentFromXML();
            persistents.add(persistent);
            System.out.println(persistent.getClassName()+" built successfully !");

        }
        System.out.println("FINISHED :: "+persistents.size()+" Objects created !");
        setPersistents(persistents);
        return persistents;
    }
}
