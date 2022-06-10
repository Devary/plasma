package com.atlassian.jira.rest.client.app;

import com.atlassian.jira.rest.client.api.domain.Issue;
import org.joda.time.DateTime;

import java.sql.Time;
import java.sql.Timestamp;

public class PlasmaQuery {
    private transient String sql;
    private transient int status = 0;
    private transient String description;//to be filled with body
    private String issue_id;
    private Issue issue;
    private String owner;
    private int type;
    private int module_id;
    private long project_id;
    private DateTime created_at;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(String issue_id) {
        this.issue_id = issue_id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    public boolean validate() {
        return true;
    }

    public DateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(DateTime created_at) {
        this.created_at = created_at;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public static final class PlasmaQueryBuilder {
        private transient String sql;
        private transient int status = 0;
        private transient String description;//to be filled with body
        private String issue_id;
        private Issue issue;
        private String owner;
        private int type;
        private int module_id;
        private Long project_id;
        private DateTime created_at;

        public PlasmaQueryBuilder() {
        }

        public static PlasmaQueryBuilder aPlasmaQuery() {
            return new PlasmaQueryBuilder();
        }

        public PlasmaQueryBuilder withSql(String sql) {
            this.sql = sql;
            return this;
        }

        public PlasmaQueryBuilder withStatus(int status) {
            this.status = status;
            return this;
        }

        public PlasmaQueryBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public PlasmaQueryBuilder withIssue_id(String issue_id) {
            this.issue_id = issue_id;
            return this;
        }

        public PlasmaQueryBuilder withOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public PlasmaQueryBuilder withType(int type) {
            this.type = type;
            return this;
        }

        public PlasmaQueryBuilder withModule_id(int module_id) {
            this.module_id = module_id;
            return this;
        }

        public PlasmaQueryBuilder withProject_id(Long project_id) {
            this.project_id = project_id;
            return this;
        }
        public PlasmaQueryBuilder withCreated_at(DateTime created_at) {
            this.created_at = created_at;
            return this;
        }
        public PlasmaQueryBuilder withIssue(Issue issue) {
            this.issue = issue;
            return this;
        }

        public PlasmaQuery build() {
            PlasmaQuery plasmaQuery = new PlasmaQuery();
            plasmaQuery.setSql(sql);
            plasmaQuery.setStatus(status);
            plasmaQuery.setDescription(description);
            plasmaQuery.setIssue_id(issue_id);
            plasmaQuery.setOwner(owner);
            plasmaQuery.setType(type);
            plasmaQuery.setModule_id(module_id);
            plasmaQuery.setProject_id(project_id);
            plasmaQuery.setCreated_at(created_at);
            plasmaQuery.setIssue(issue);
            return plasmaQuery;
        }
    }
}
