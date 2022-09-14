package com.atlassian.jira.rest.client.app;

import java.net.URI;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Component;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import io.atlassian.util.concurrent.Promise;
import org.apache.http.client.utils.DateUtils;

public class Processor {
    public static void main(String[] args) throws Exception {
        getIssue();
    }

    public static void getIssue() throws Exception {
        Instant start = Instant.now();
// CODE HERE
        final URI jiraServerUri = new URI("https://jira.vermeg.com");
        final JiraRestClient restClient = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(jiraServerUri, "fhammami", "###Plasma###1803");
        final IssueRestClient issueRestClient = restClient.getIssueClient();
        ArrayList<PlasmaQuery> pqs = new ArrayList<>();
        //Persist projects
        Promise projectPromise = restClient.getProjectClient().getAllProjects();

        Collection<BasicProject> basicProjects = (Collection<BasicProject>) projectPromise.claim();
        for (BasicProject project : basicProjects) {
            DatabaseService dbs = new DatabaseService(project, PersistentType.PROJECT);
            dbs.closeConnection();
            int j = 1;
            //System.out.println(j);
            String projectKey = project.getKey();
            //com.atlassian.jira.rest.client.api.domain.Project basicProject = (com.atlassian.jira.rest.client.api.domain.Project) project;
            try{
                Iterable<BasicComponent> components = restClient.getProjectClient().getProject(projectKey).get().getComponents();
                dbs = new DatabaseService(components, PersistentType.COMPONENT);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            ArrayList<com.atlassian.jira.rest.client.api.domain.BasicComponent> newCom = new ArrayList();
            //newCom.add(new com.atlassian.jira.rest.client.api.domain.BasicComponent(null, new Long(1), "NONE", ""));
            //new DatabaseService(newCom, PersistentType.COMPONENT);
            while (true) {
                Promise issuePromise = issueRestClient.getIssue(projectKey + "-" + j);
                Issue issue;
                try {
                    issue = (Issue) issuePromise.claim();
                    if (!issue.getComments().iterator().hasNext()) {
                        j++;
                        continue;
                    }
                } catch (Exception e) {
                    if (!pqs.isEmpty()) {
                        dbs = new DatabaseService(pqs, PersistentType.QUERY);
                    }
                    break;
                }
                ParsingService ps = new ParsingService(issue, false);
                pqs.addAll(ps.getSqls());

                System.out.println("Issue : " + projectKey + "-" + j + " Completed");

                //commit cycle
                dbs = new DatabaseService(pqs, PersistentType.QUERY);
                //reinit
                pqs = new ArrayList<>();
                j++;
                dbs.closeConnection();
            }
        }

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("Finished ::  time elapsed :  " + new Time(timeElapsed));
    }

}
