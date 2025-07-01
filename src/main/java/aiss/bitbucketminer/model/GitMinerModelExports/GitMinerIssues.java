package aiss.bitbucketminer.model.GitMinerModelExports;

import com.fasterxml.jackson.annotation.JsonIgnore;


import java.util.*;

public class GitMinerIssues {

    private String id;

    private String title;

    private String description;

    private String state;

    private String created_at;

    private String updated_at;

    private String closed_at;

    private List<String> labels;

    private Integer votes;

    private List<GitMinerComment> comments;

    private GitMinerUser assignee;

    @JsonIgnore
    private GitMinerProject gitminerproject;

    private GitMinerUser author;

    private String kind;

    private Integer watches;


    // Constructor vacío
    public GitMinerIssues() {
        this.comments = new ArrayList<>();
    }

    // Constructor con parámetros
    public GitMinerIssues(String id, String title, String description, String state, String created_at,
                          String updated_at, String closed_at, List<String> labels,
                          Integer votes, String kind, Integer watches) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.state = state;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.closed_at = closed_at;
        this.labels = labels;
        this.votes = votes;
        this.comments = new ArrayList<>();
        this.author= new GitMinerUser();
        this.assignee = new GitMinerUser();
        this.kind = kind;
        this.watches = watches;
    }

    public GitMinerUser getAssignee() {
        return assignee;
    }

    public void setAssignee(GitMinerUser assignee) {
        this.assignee = assignee;
    }

    public List<GitMinerComment> getComments() {
        return comments;
    }

    public void setComments(List<GitMinerComment> comments) {
        this.comments = comments;
    }

    public List<GitMinerComment> comments() {
        return comments;
    }

    public void setGitMinerComments(List<GitMinerComment> comments) {
        this.comments = comments;
    }

    public GitMinerUser getAuthor() {
        return author;
    }

    public void setAuthor(GitMinerUser author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getClosed_at() {
        return closed_at;
    }

    public void setClosed_at(String closed_at) {
        this.closed_at = closed_at;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public void setProject(GitMinerProject gitMinerProject) {
        this.gitminerproject = gitMinerProject;
    }

    public String getKind() {return kind;}

    public void setKind(String kind) {this.kind = kind;}

    public Integer getWatches() {return watches;}

    public void setWatches(Integer watches) {this.watches = watches;}
}