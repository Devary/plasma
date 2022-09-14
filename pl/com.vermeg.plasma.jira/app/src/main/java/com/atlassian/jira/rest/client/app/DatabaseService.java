package com.atlassian.jira.rest.client.app;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class DatabaseService {

    private Connection conn = null;

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

    public DatabaseService(Object object, String persistentType) {
        connect();
        if (PersistentType.QUERY.equals(persistentType)) {
            List<PlasmaQuery> queries = (List<PlasmaQuery>) object;
            for (PlasmaQuery pq : queries) {
                if (pq.validate()) {
                    persistQuery(pq);
                }
            }
        } else if (PersistentType.PROJECT.equals(persistentType)) {
            //com.atlassian.jira.rest.client.api.domain.Project project = (com.atlassian.jira.rest.client.api.domain.Project) object;
            persistProject((BasicProject)object);
        } else if (PersistentType.COMPONENT.equals(persistentType)) {
            ArrayList<com.atlassian.jira.rest.client.api.domain.BasicComponent> components = (ArrayList<com.atlassian.jira.rest.client.api.domain.BasicComponent>) object;
            for (com.atlassian.jira.rest.client.api.domain.BasicComponent component : components) {
                persistComponent(component);
            }

        }
    }

    private void persistQuery(PlasmaQuery pq) {
        StringBuilder insertion = new StringBuilder("INSERT INTO public.jira_query (status, creation_date, \"ticketNumber\", owner, type, created_at, module_id, project_id,description,entities) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?);");
        try {
            PreparedStatement statement = conn.prepareStatement(insertion.toString());
            statement.setInt(1, 0);
            Timestamp timestamp2 = new Timestamp(pq.getCreated_at().getMillis());

            statement.setTimestamp(2, timestamp2);
            statement.setString(3, pq.getIssue_id());
            statement.setString(4, pq.getOwner());
            statement.setInt(5, pq.getType());
            java.util.Date date = new Date();
            timestamp2 = new Timestamp(date.getTime());
            statement.setTimestamp(6, timestamp2);
            statement.setInt(7, 1);
            statement.setInt(8, pq.getIssue().getProject().getId().intValue());
            statement.setString(9, pq.getSql());
            statement.setString(10, "");


            int update = statement.executeUpdate();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void persistProject(BasicProject project) {
        StringBuilder insertion = new StringBuilder("INSERT INTO public.project (id,name, slug, created_at, updated_at) VALUES (?,?, ?, ?, ?);");
        try {
            PreparedStatement statement = conn.prepareStatement(insertion.toString());
            statement.setInt(1, project.getId().intValue());
            statement.setString(2, project.getName());
            statement.setString(3, project.getKey());
            java.util.Date date = new Date();
            Timestamp timestamp2 = new Timestamp(date.getTime());
            statement.setTimestamp(4, timestamp2);
            statement.setTimestamp(5, null);

            int update = statement.executeUpdate();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private void persistComponent(com.atlassian.jira.rest.client.api.domain.BasicComponent component) {
        StringBuilder insertion = new StringBuilder("INSERT INTO public.component (id,name, created_at, updated_at) VALUES (?,?, ?, ?);");
        try {
            PreparedStatement statement = conn.prepareStatement(insertion.toString());
            statement.setInt(1, component.getId().intValue());
            statement.setString(2, component.getName());
            java.util.Date date = new Date();
            Timestamp timestamp2 = new Timestamp(date.getTime());
            statement.setTimestamp(3, timestamp2);
            statement.setTimestamp(4, null);

            int update = statement.executeUpdate();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void closeConnection(){
        try {
            this.conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
