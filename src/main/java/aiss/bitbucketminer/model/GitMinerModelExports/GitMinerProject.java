package aiss.bitbucketminer.model.GitMinerModelExports;



import java.util.ArrayList;
import java.util.List;

public class GitMinerProject {

    private String id;

    private String name;

    private String web_url;

    private List<GitMinerCommit> commits= new ArrayList<>();

    private List<GitMinerIssues> issues;

    private String language;

    private Boolean isPrivate;

    public GitMinerProject() {

    }

    public GitMinerProject(String id, String name, String web_url, String language, Boolean isPrivate) {
        this.id= id;
        this.name = name;
        this.web_url = web_url;
        this.issues = new ArrayList<>();
        this.commits = new ArrayList<>();
        this.language = language;
        this.isPrivate = isPrivate;
    }

    public void addCommit(GitMinerCommit commit) {
        commits.add(commit);
    }
    public List<GitMinerCommit> commits() {
        return commits;
    }

    public void setGitMinercommits(List<GitMinerCommit> commits) {
        this.commits = commits;
    }

    public void addIssue(GitMinerIssues issue) {
        issues.add(issue);
        issue.setProject(this);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public List<GitMinerCommit> getcommits() {
        return commits;
    }

    public void setcommits(List<GitMinerCommit> gitMinercommits) {
        this.commits = gitMinercommits;
    }

    public List<GitMinerIssues> getIssues() {
        return issues;
    }

    public void setIssues(List<GitMinerIssues> issues) {
        this.issues = issues;


    }

    public String getLanguage() {return language;}
    public void setLanguage(String language) {this.language = language;}

    public Boolean getIsPrivate() { return isPrivate; }
    public void setIsPrivate(Boolean isPrivate) { this.isPrivate = isPrivate; }
}
