/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import Engines.connections.PostgresqlConnection;
import hierarchy.persistence.Persistent;
import files.IAbstractFile;
import hierarchy.persistence.types.Field;
import hierarchy.persistence.types.Link;
import projects.ProjectFile;
import projects.ProjectImpl;
import services.parsing.ParsingService;
import services.reporting.Report;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class PersistenceObjectsCreationProcess implements IAbstractProcess {

    private Collection<Persistent> persistents;
    private Connection conn = null;
    long id=-1;
    public Collection<Persistent> getPersistents() {
        return persistents;
    }

    private void setPersistents(Collection<Persistent> persistents) {
        this.persistents = persistents;
    }

    @Override
    public ProjectImpl createProject(String basePath) {
        return null;
    }

    @Override
    public IAbstractFile createAbstractFile() {
        return null;
    }

    @Override
    public ArrayList<Persistent> createObjectFiles(ArrayList<ProjectFile> projectPersistenceFiles, Report report) throws Exception {
        System.out.println("Processing Object creation");
        ArrayList<Persistent> persistents = new ArrayList<>();
        int max = 5000; // for dev matters , must be removed after tests
        int i = 0;
        for (ProjectFile projectFile : projectPersistenceFiles) {
            if (i == max) { // for dev matters
                break;
            }
            ParsingService ps = new ParsingService(projectFile);
            Persistent persistent = ps.buildPersistentFromXML();
            //boolean isValid = persistent.isValid();
            if (persistent.isValid()) {
                //TODO: check for validity
                seedToDb(persistent);
            } else {
                System.out.println("Persistent " + persistent.toString() + " is not valid");

                //throw new Exception("Persistent " + persistent.toString() + " is not valid");
            }
            persistents.add(persistent);
            System.out.println(persistent.getClassName() + " built successfully !");
            i++;

        }
        //updateLinksElementTypes(); not needed right now
        System.out.println("FINISHED :: " + persistents.size() + " Objects created !");
        setPersistents(persistents);
        return persistents;
    }

    public void updateLinksElementTypes() {
        try {
            //get All persistent from database
            connect();
            Statement statement = conn.createStatement();
            ResultSet rsPers = statement.executeQuery("SELECT * FROM PERSISTENT WHERE 1=1");
            HashMap<String, Integer> persistentList = new HashMap<>();
            while (rsPers.next()) {
                int id = rsPers.getInt("id");
                String name = rsPers.getString("name");
                persistentList.put(name, id);
            }

            ResultSet rsLinks = statement.executeQuery("SELECT * FROM LINKS WHERE 1=1");
            while (rsLinks.next()) {
                int id = rsLinks.getInt("id");
                String elementType = rsLinks.getString("elementType");
                if (elementType != null) {
                    elementType = elementType.substring(elementType.lastIndexOf(".")+1);
                    Integer idper = persistentList.get(elementType);
                    if (idper != null){ //for dev matters
                        PreparedStatement statement2 = conn.prepareStatement("UPDATE LINKS SET \"elementType_id\" = ? where id = ?");
                        statement2.setInt(1,idper);
                        statement2.setInt(2,id);
                        statement2.executeUpdate();
                        statement2.closeOnCompletion();

                    }

                }
            }
            statement.closeOnCompletion();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //get All links from database

        // loop on links
        // find a persistent with name of 'elementype' of the link
        // set the persistent id to the link element_id
        // update the link
    }

    private void seedToDb(Persistent persistent) {
        seedPersistent(persistent);
        int id = getMaxId("persistent");
        if (!persistent.getLinks().isEmpty()) {
            SeedLinks(persistent, id);
        }

        //SeedQueries(persistent,id);
        //SeedCodes(persistent,id);
        if (!persistent.getFields().isEmpty()) {
            SeedFields(persistent, id);
        }
    }

    private void SeedLinks(Persistent persistent, long id) {
        String SEPARATOR = ",";
        connect();


        for (Link link : persistent.getLinks()) {

            StringBuilder insertion = new StringBuilder("INSERT INTO public.links (" +
                    "name, " +
                    "dbname, " +
                    "dbtype, " +
                    "\"collectionType\", " +
                    "\"elementType\", " +
                    "\"allowsNull\", " +
                    "\"inverseName\", " +
                    "created_at, " +
                    "updated_at, " +
                    "\"elementType_id\", " +
                    "persistent_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)");

            try {
                PreparedStatement statement = conn.prepareStatement(insertion.toString());


                statement.setString(1, link.getName());


                statement.setString(2, link.getDbname());


                statement.setString(3, link.getDbtype());


                //TODO:: integrity check
                statement.setString(4, link.getCollectionType());
                statement.setString(5, link.getElementType());


                statement.setBoolean(6, link.isAllowNulls());

                if (link.getInverseName() != null) {
                    statement.setString(7, link.getInverseName());
                }
                Date date = new Date();
                Timestamp timestamp2 = new Timestamp(date.getTime());
                statement.setTimestamp(8, timestamp2);
                statement.setTimestamp(9, null);
                statement.setLong(10, id);//element_id must be changed ==> batch at the end of the process to update elements types
                statement.setLong(11, id);

                statement.executeUpdate();
                statement.close();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }

    private final String url = "jdbc:postgresql://localhost:5432/test";
    private final String user = "postgres";
    private final String password = "admin";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return conn;

    }

    private long seedPersistent(Persistent persistent) {
        StringBuilder insertion = new StringBuilder("INSERT INTO persistent (" +
                "name, " +
                "mapping, " +
                "short_table_name, " +
                "\"table\", " +
                "created_at, " +
                "is_persistent, " +
                "updated_at) VALUES (?,?,?,?,?,?,?)");
        Date date = new Date();
        Timestamp timestamp2 = new Timestamp(date.getTime());
        //if (id == -1) {
        //    id = getMaxId("persistent");
        //}
        connect();
        try {
            PreparedStatement statement = conn.prepareStatement(insertion.toString());
            statement.setString(1,persistent.getName());
            statement.setString(2,persistent.getMappingType()!=null ? persistent.getMappingType().toUpperCase():"HIERARCHICAL");
            statement.setString(3,persistent.getShortTableName());
            statement.setString(4,persistent.getTable());
            statement.setTimestamp(5,timestamp2);
            statement.setBoolean(5,persistent.isPersistent());
            statement.setTimestamp(6,null);
            int update = statement.executeUpdate();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return id;
    }
    private int getMaxId(String tableName) {
        connect();
        StringBuilder insertion = new StringBuilder("SELECT max(id) from " + tableName);
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(insertion.toString());
            if (rs != null && rs.next()) {
                return rs.getInt(1);
            }
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }
    private void SeedFields(Persistent persistent, long persistentId) {
        connect();


        for (Field field : persistent.getFields()) {

            StringBuilder insertion = new StringBuilder("INSERT INTO field " +
                    "(name," +
                    " dbname," +
                    " dbtype," +
                    " \"allowsNull\"," +
                    " dbsize," +
                    " dbscale," +
                    " \"defaultValue\"," +
                    " created_at," +
                    " updated_at," +
                    " persistent_id" +
                    ") VALUES (?,?,?,?,?,?,?,?,?,?)");

            Date date = new Date();
            Timestamp timestamp2 = new Timestamp(date.getTime());
            try {
                PreparedStatement statement = conn.prepareStatement(insertion.toString());
                statement.setString(1,field.getName());
                statement.setString(2,field.getDbname());
                statement.setString(3,field.getDbtype());
                statement.setBoolean(4,field.isAllowNulls());
                statement.setString(5,field.getDbsize());
                statement.setString(6,field.getDbscale());
                statement.setString(7,field.getDefaultValue());
                statement.setTimestamp(8,timestamp2);
                statement.setTimestamp(9,null);
                statement.setLong(10,persistentId);
                statement.executeUpdate();
                statement.close();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }
}
