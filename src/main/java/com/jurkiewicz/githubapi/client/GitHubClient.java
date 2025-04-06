package com.jurkiewicz.githubapi.client;

import com.jurkiewicz.githubapi.model.Branch;
import com.jurkiewicz.githubapi.model.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GitHubClient {

    @Value("${github.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    private static final String USER_REPOSITORIES_URL = "/users/{username}/repos";
    private static final String REPOSITORY_BRANCHES_URL = "/repos/{username}/{repo}/branches";

    public List<Repository> fetchUserRepositories(String username) {
        String url = apiUrl + USER_REPOSITORIES_URL;
        ResponseEntity<List<Repository>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                username
        );
        return Objects.nonNull(response.getBody()) ? response.getBody() : List.of();
    }

    public List<Branch> fetchRepositoryBranches(String username, String repositoryName) {
        String url = apiUrl + REPOSITORY_BRANCHES_URL;
        ResponseEntity<List<Branch>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                username,
                repositoryName
        );
        return Objects.nonNull(response.getBody()) ? response.getBody() : List.of();
    }
}
