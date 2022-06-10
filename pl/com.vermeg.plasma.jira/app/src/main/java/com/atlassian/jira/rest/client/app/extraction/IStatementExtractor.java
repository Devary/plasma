package com.atlassian.jira.rest.client.app.extraction;

public interface IStatementExtractor {
    void createRules();
    void validate();
    void getStatement();
}
