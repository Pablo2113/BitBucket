package aiss.bitbucketminer.controller;

import aiss.bitbucketminer.model.COMMENT_POJO.Comments;
import aiss.bitbucketminer.model.GitMinerModelExports.*;
import aiss.bitbucketminer.model.COMMIT_POJO.Commit;
import aiss.bitbucketminer.model.ISSUES_POJO.Issues;
import aiss.bitbucketminer.model.ISSUES_POJO.Value;
import aiss.bitbucketminer.model.USER_POJO.Users;
import aiss.bitbucketminer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import aiss.bitbucketminer.model.REPOSITORY_POJO.Commit_Repository;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/bitbucket")
public class BitbucketController {


    private static final Logger log = LoggerFactory.getLogger(BitbucketController.class);

    @Autowired
    private BitbucketService bitbucketService;

    @Autowired
    private ProjectTransformerService projectTransformerService;

    @Autowired
    private CommitTransformerService CommitTransformerService;

    private final RestTemplate restTemplate = new RestTemplate();


    @GetMapping("/{workspace}/{repoSlug}/commits")
    public ResponseEntity<List<GitMinerCommit>> getCommits(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(defaultValue = "5") Integer nCommits,
            @RequestParam(defaultValue = "2") Integer maxPages) {

        Commit_Repository repoData = bitbucketService.getProjectsFromBitbucket(workspace, repoSlug);

        GitMinerProject project = projectTransformerService.transform(repoData, new ArrayList<>(), new ArrayList<>());

        String projectName = project.getName();

        Commit commitData = bitbucketService.getCommitsFromBitbucket(workspace, repoSlug, nCommits, maxPages);
        //Transformamos esos datos al formato GitMiner
        List<GitMinerCommit> gitMinerCommits = CommitTransformerService.transform(commitData);

        //Devolvemos los datos transformados
        return ResponseEntity.ok(gitMinerCommits);
    }

    @GetMapping("/{workspace}/{repoSlug}")
    public ResponseEntity<GitMinerProject> getProjects(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(defaultValue = "5") Integer nCommits,
            @RequestParam(defaultValue = "2") Integer maxPages,
            @RequestParam(defaultValue = "5") Integer nIssues
) {
        log.info("Starting to retrieve project data from Bitbucket for repo: {}/{}", workspace, repoSlug);


        Commit_Repository repoData = bitbucketService.getProjectsFromBitbucket(workspace, repoSlug);
        log.info("Repository data retrieved successfully.");

        Commit commitData = bitbucketService.getCommitsFromBitbucket(workspace, repoSlug, nCommits, maxPages);
        log.info("Commits transformed successfully. Total: {}", nCommits);

        List<GitMinerCommit> commits = CommitTransformerService.transform(commitData);

        Issues issuesData = bitbucketService.getIssuesFromBitbucket(workspace, repoSlug,nIssues,maxPages);
        log.info("Issue data retrieved. Processing issues...");
        List<GitMinerIssues> issues = new ArrayList<>();
        List<GitMinerUser> allusers = new ArrayList<>();

        for (Value issueValue : issuesData.getValues()) {
            Users reporterData = bitbucketService.getUserFromBitbucket(issueValue.getReporter().getAccountId());
            GitMinerUser reporter = UserTransformerService.transform(reporterData);
            allusers.add(reporter);

            Comments commentsData = bitbucketService.getCommentsFromBitbucket(workspace, repoSlug, issueValue.getId());
            List<GitMinerComment> comments = new ArrayList<>();

            if (commentsData != null && commentsData.getValues() != null) {
                for (aiss.bitbucketminer.model.COMMENT_POJO.Value commentValue : commentsData.getValues()) {
                    Users commentUserData = bitbucketService.getUserFromBitbucket(commentValue.getUser().getAccountId());
                    GitMinerUser commentUser = UserTransformerService.transform(commentUserData);

                    GitMinerComment comment = CommentsTransformService.transform(commentValue, issueValue.getId(), commentUser);
                    if (comment != null) {
                        comments.add(comment);
                    }
                }
            }
            log.info("Issues and comments processed. Filtering unique issues...");

            List<GitMinerIssues> transformedIssues = IssuesTransformerService.transform(issuesData, comments, allusers);
            if (transformedIssues != null && !transformedIssues.isEmpty()) {
                issues.addAll(transformedIssues);
            }
        }
        List<GitMinerIssues> ListaIssues = new ArrayList<>();
        Set<String> uniqueIds = new HashSet<>();

        for (GitMinerIssues issue : issues) {
            if (uniqueIds.add(issue.getId())) {
                ListaIssues.add(issue);
            }
        }

        log.info("Final issue list prepared. Total unique issues: {}", ListaIssues.size());
        GitMinerProject project = projectTransformerService.transform(repoData, commits, ListaIssues);
        log.info("Project transformed successfully. Returning response.");
        return ResponseEntity.ok(project);
    }

    @GetMapping("/{workspace}/{repoSlug}/issues")
    public ResponseEntity<List<GitMinerIssues>> getIssues(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(required = false) Integer nCommits,
            @RequestParam(required = false) Integer maxPages
            ) {

        // Obtener datos de los issues
        Issues issuesData = bitbucketService.getIssuesFromBitbucket(workspace, repoSlug,nCommits,maxPages);
        List<GitMinerIssues> resultado = new ArrayList<>();

        // Verificar si se obtuvieron issues
        if (issuesData != null && issuesData.getValues() != null) {
            // Obtener los comentarios y usuarios de todos los issues
            List<GitMinerComment> allComments = new ArrayList<>();
            List<GitMinerUser> allUsers = new ArrayList<>();

            for (Value value : issuesData.getValues()) {
                Users userData = bitbucketService.getUserFromBitbucket(value.getReporter().getAccountId());
                GitMinerUser gitMinerUser = UserTransformerService.transform(userData);
                allUsers.add(gitMinerUser);
                Comments commentsData = bitbucketService.getCommentsFromBitbucket(workspace, repoSlug, value.getId());
                if (commentsData != null && commentsData.getValues() != null) {
                    for (aiss.bitbucketminer.model.COMMENT_POJO.Value commentValue : commentsData.getValues()) {
                        Users commentUserData = bitbucketService.getUserFromBitbucket(commentValue.getUser().getAccountId());
                        GitMinerUser commentUser = UserTransformerService.transform(commentUserData);
                        GitMinerComment comment = CommentsTransformService.transform(commentValue, value.getId(), commentUser);
                        if (comment != null) {
                            allComments.add(comment);
                        }
                    }
                }
            }

            // Transformar todos los issues
            resultado = IssuesTransformerService.transform(issuesData, allComments, allUsers);
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{workspace}/{repoSlug}/issues/{issuesId}/comments")
    public ResponseEntity<List<GitMinerComment>> getComments(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @PathVariable Integer issuesId,
            @RequestParam(required = false) Integer nCommits,
            @RequestParam(required = false) Integer maxPages) {

        Comments commentsData = bitbucketService.getCommentsFromBitbucket(workspace, repoSlug, issuesId);
        List<GitMinerComment> result = new ArrayList<>();

        for (aiss.bitbucketminer.model.COMMENT_POJO.Value value : commentsData.getValues()) {
            Users userData = bitbucketService.getUserFromBitbucket(value.getUser().getAccountId());
            GitMinerUser gitMinerUser = UserTransformerService.transform(userData);

            GitMinerComment comment = CommentsTransformService.transform(value, issuesId, gitMinerUser);
            if (comment != null) {
                result.add(comment);
            }
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/{userId}")
        public ResponseEntity<GitMinerUser> getUser(
                @PathVariable String userId,
                @RequestParam(required = false) Integer nCommits,
                @RequestParam(required = false) Integer maxPages) {
        // Obtener datos del usuario
        Users userdata = bitbucketService.getUserFromBitbucket(userId);

        // Transformar datos del usuario
        GitMinerUser gitMinerUser = UserTransformerService.transform(userdata);

        return ResponseEntity.ok(gitMinerUser);

    }

    //----------------------------------------PETICIONES POST-----------------------------------------------------------




    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{workspace}/{repoSlug}")
    public ResponseEntity<GitMinerProject> saveProjectToDatabase(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(defaultValue = "5") Integer nCommits,
            @RequestParam(defaultValue = "2") Integer maxPages,
            @RequestParam(defaultValue = "5") Integer nIssues
    ) {

        log.info("Starting POST process for repository: {}/{}", workspace, repoSlug);

        // Obtener datos del proyecto
        Commit_Repository repoData = bitbucketService.getProjectsFromBitbucket(workspace, repoSlug);
        log.info("Repository data retrieved successfully.");

        Commit commitData = bitbucketService.getCommitsFromBitbucket(workspace, repoSlug, nCommits, maxPages);
        log.info("Commit data retrieved. Transforming commits...");

        List<GitMinerCommit> commits = CommitTransformerService.transform(commitData);
        log.info("Commits transformed successfully. Total: {}", nCommits);

        Issues issuesData = bitbucketService.getIssuesFromBitbucket(workspace, repoSlug,nIssues,maxPages);
        log.info("Issue data retrieved. Processing issues...");

        List<GitMinerIssues> issues = new ArrayList<>();
        List<GitMinerUser> allusers = new ArrayList<>();

        for (Value issueValue : issuesData.getValues()) {
            Users reporterData = bitbucketService.getUserFromBitbucket(issueValue.getReporter().getAccountId());
            GitMinerUser reporter = UserTransformerService.transform(reporterData);
            allusers.add(reporter);

            Comments commentsData = bitbucketService.getCommentsFromBitbucket(workspace, repoSlug, issueValue.getId());
            List<GitMinerComment> comments = new ArrayList<>();

            if (commentsData != null && commentsData.getValues() != null) {
                for (aiss.bitbucketminer.model.COMMENT_POJO.Value commentValue : commentsData.getValues()) {
                    Users commentUserData = bitbucketService.getUserFromBitbucket(commentValue.getUser().getAccountId());
                    GitMinerUser commentUser = UserTransformerService.transform(commentUserData);

                    GitMinerComment comment = CommentsTransformService.transform(commentValue, issueValue.getId(), commentUser);
                    if (comment != null) {
                        comments.add(comment);
                    }
                }
            }

            List<GitMinerIssues> transformedIssues = IssuesTransformerService.transform(issuesData, comments, allusers);
            if (transformedIssues != null && !transformedIssues.isEmpty()) {
                issues.addAll(transformedIssues);
            }
        }

        log.info("Issues and comments processed. Filtering unique issues...");
        List<GitMinerIssues> ListaIssues = new ArrayList<>();
        Set<String> uniqueIds = new HashSet<>();

        for (GitMinerIssues issue : issues) {
            if (uniqueIds.add(issue.getId())) {
                ListaIssues.add(issue);
            }
        }
        log.info("Final issue list prepared. Total unique issues: {}", ListaIssues.size());

        GitMinerProject project = projectTransformerService.transform(repoData, commits, ListaIssues);
        log.info("Project transformed successfully. Sending POST request to GitMiner...");

        // URL del servicio donde se guardará el proyecto
        String postUrl = "http://localhost:8080/gitminer/projects";


        // Realizar la petición POST
        ResponseEntity<GitMinerProject> response = restTemplate.postForEntity(postUrl, project, GitMinerProject.class);
        log.info("POST request sent. Received status: {}", response.getStatusCode());

        // Devolver el estado y un mensaje de éxito
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }






}

