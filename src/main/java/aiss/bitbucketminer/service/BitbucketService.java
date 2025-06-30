package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.COMMENT_POJO.Comments;
import aiss.bitbucketminer.model.COMMIT_POJO.Commit;
import aiss.bitbucketminer.model.ISSUES_POJO.Issues;
import aiss.bitbucketminer.model.REPOSITORY_POJO.Commit_Repository;
import aiss.bitbucketminer.model.USER_POJO.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Service
public class BitbucketService {

    private static final Logger logger = LoggerFactory.getLogger(BitbucketService.class);
    private final RestTemplate restTemplate;
    private static final String BITBUCKET_API_BASE_URL = "https://api.bitbucket.org/2.0";
    private final String username = "modal175175";
    private final String appPassword = "ATBBrq8du6NpP9ajc2vgnu2GXKFQA13A5029";


    public BitbucketService() {
        this.restTemplate = new RestTemplate();
    }

    public Commit getCommitsFromBitbucket(String workspace, String repoSlug, Integer nCommits, Integer maxPages) {
        String url = BITBUCKET_API_BASE_URL
                + "/repositories/" + workspace + "/" + repoSlug + "/commits";
        if (nCommits != null) {
            url += "?pagelen=" + nCommits;
        }
        if (maxPages != null) {
            url += "&page=" + maxPages;
        }

        try {
            // Petición GET simple usando RestTemplate
            Commit response = restTemplate.getForObject(url, Commit.class);

            if (response == null) {
                logger.warn("La respuesta de la API de Bitbucket es nula para la URL: {}", url);
                return null;
            }
            return response;
        } catch (RestClientException e) {
            logger.error("Error al realizar la solicitud a la API de Bitbucket: {}", e.getMessage());
            return null;
        }
    }

    public Commit_Repository getProjectsFromBitbucket(String workspace, String repoSlug) {
        String url = BITBUCKET_API_BASE_URL
                + "/repositories/" + workspace + "/" + repoSlug;

        try {

            Commit_Repository response = restTemplate.getForObject(url, Commit_Repository.class);

            if (response == null) {
                logger.warn("La respuesta de la API de Bitbucket es nula para la URL: {}", url);
                return null;
            }

            return response;
        } catch (RestClientException e) {
            logger.error("Error al realizar la solicitud a la API de Bitbucket: {}", e.getMessage());
            return null;
        }
    }

    public Issues getIssuesFromBitbucket(String workspace, String repoSlug, Integer nIssues, Integer maxPages) {
        String url = BITBUCKET_API_BASE_URL
                + "/repositories/" + workspace + "/" + repoSlug + "/issues";
        if (nIssues != null) {
            url += "?pagelen=" + nIssues;
        }
        if (maxPages != null) {
            url += "&page=" + maxPages;
        }

        try {
            Issues response = restTemplate.getForObject(url, Issues.class);

            if (response == null) {
                logger.warn("La respuesta de la API de Bitbucket es nula para la URL: {}", url);
                return null;
            }

            return response;
        } catch (RestClientException e) {
            logger.error("Error al realizar la solicitud a la API de Bitbucket: {}", e.getMessage());
            return null;
        }
    }

    public Comments getCommentsFromBitbucket(String workspace, String repoSlug, Integer id) {
        String url = BITBUCKET_API_BASE_URL
                + "/repositories/" + workspace + "/" + repoSlug + "/issues/" + id + "/comments";

        try {

            Comments response = restTemplate.getForObject(url, Comments.class);

            if (response == null) {
                logger.warn("La respuesta de la API de Bitbucket es nula para la URL: {}", url);
                return null;
            }

            return response;
        } catch (RestClientException e) {
            logger.error("Error al realizar la solicitud a la API de Bitbucket: {}", e.getMessage());
            return null;
        }
    }


    public String encodeUUID(String uuid) throws UnsupportedEncodingException {
        return URLEncoder.encode(uuid, "UTF-8");
    }



    //PARA ESTA PETICIÓN ES TOTALMENTE NECESARIO TENER UNA CUENTA DE BITBUCKET Y POSEER UNA APP PASSWORD!!!
    public Users getUserFromBitbucket(String id) {
        String url = BITBUCKET_API_BASE_URL + "/users/" + id;

        try {
            String auth = username + ":" + appPassword;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + encodedAuth);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Users> response = restTemplate.exchange(url, HttpMethod.GET, entity, Users.class);
            if (response.getBody() == null) {
                logger.warn("La respuesta de la API de Bitbucket es nula para la URL: {}", url);
                return null;
            }

            return response.getBody();
        } catch (RestClientException e) {
            logger.error("Error al realizar la solicitud a la API de Bitbucket: {}", e.getMessage());
            return null;
        }
    }










}
