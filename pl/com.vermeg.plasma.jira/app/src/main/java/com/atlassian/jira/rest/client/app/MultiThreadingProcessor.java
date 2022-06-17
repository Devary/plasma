package com.atlassian.jira.rest.client.app;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import io.atlassian.util.concurrent.Promise;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class MultiThreadingProcessor extends Thread{
    private int min=0;
    private int max=0;

    public MultiThreadingProcessor(final int min,final int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void run() {
        try {
            startProcessor();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void startProcessor() throws URISyntaxException {
        Instant start = Instant.now();
// CODE HERE
        final URI jiraServerUri = new URI("https://jira.vermeg.com");
        final JiraRestClient restClient = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(jiraServerUri, "fhammami", "###Rettila###1510");
        final IssueRestClient issueRestClient = restClient.getIssueClient();
        ArrayList<PlasmaQuery> pqs = new ArrayList<>();
        Promise projectPromise = restClient.getProjectClient().getAllProjects();
        DatabaseService dbs = new DatabaseService(projectPromise.claim(), PersistentType.PROJECT);
        dbs.closeConnection();
        for (BasicProject basicProject : (ArrayList<BasicProject>) projectPromise.claim()) {
            if (basicProject.getKey().equals("RUNCLV")){
                int j = 1;
                System.out.println(j);
                String issueKey = basicProject.getKey();
                while (true) {
                    Promise issuePromise = issueRestClient.getIssue(issueKey + "-" + j);
                    Issue issue;
                    try {
                        issue = (Issue) issuePromise.claim();
                        if (!issue.getComments().iterator().hasNext()){
                            j++;
                            continue;
                        }
                    } catch (Exception e) {
                        if (!pqs.isEmpty()){
                            dbs = new DatabaseService(pqs, PersistentType.QUERY);
                        }
                        break;
                    }
                    ParsingService ps = new ParsingService(issue, false);
                    pqs.addAll(ps.getSqls());

                    System.out.println("Issue : "+issueKey + "-" + j+" Completed");

                    if (pqs.size() > 1000) {
                        //commit cycle
                        dbs = new DatabaseService(pqs, PersistentType.QUERY);
                        //reinit
                        pqs = new ArrayList<>();
                    }
                    j++;
                    dbs.closeConnection();
                }
            }
        }

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("Finished ::  time elapsed :  " + new Time(timeElapsed));
    }
}
