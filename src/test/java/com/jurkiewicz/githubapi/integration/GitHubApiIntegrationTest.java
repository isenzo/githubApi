package com.jurkiewicz.githubapi.integration;

import com.jurkiewicz.githubapi.client.GitHubClient;
import com.jurkiewicz.githubapi.model.Branch;
import com.jurkiewicz.githubapi.model.Commit;
import com.jurkiewicz.githubapi.model.Owner;
import com.jurkiewicz.githubapi.model.Repository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GitHubApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GitHubClient githubClient;

    private static final String VALID_USERNAME = "torvalds";
    private static final String NON_FORKED_REPO = "linux";
    private static final String FORKED_REPO = "forked-repo";
    private static final String INVALID_USERNAME = "abcd581NieIstnieje";
    private static final String BRANCH_MASTER = "master";
    private static final String COMMIT_SHA = "16cd1c2657762c62a00ac78eecaa25868f7e601b";
    private static final String URI_TEMPLATE = "/api/{username}";
    private static final String NOT_FOUND_MESSAGE = "Not Found";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final Integer NOT_FOUND_STATUS = 404;

    @TestConfiguration
    static class GithubClientTestConfig {
        @Bean
        public GitHubClient githubClient() {
            return Mockito.mock(GitHubClient.class);
        }
    }

    @Test
    void testGetUserRepositories_validUser() throws Exception {
        List<Repository> repositories = createRepositories();
        List<Branch> branches = createBranches();

        Repository nonForkRepo = repositories.stream()
                .filter(repo -> !repo.fork())
                .findFirst()
                .orElseThrow();

        when(githubClient.fetchUserRepositories(VALID_USERNAME)).thenReturn(repositories);
        when(githubClient.fetchRepositoryBranches(VALID_USERNAME, nonForkRepo.name())).thenReturn(branches);

        mockMvc.perform(get(URI_TEMPLATE, VALID_USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].repositoryName").value(NON_FORKED_REPO))
                .andExpect(jsonPath("$[0].ownerLogin").value(VALID_USERNAME))
                .andExpect(jsonPath("$[0].branches[0].name").value(BRANCH_MASTER))
                .andExpect(jsonPath("$[0].branches[0].commit.sha").value(COMMIT_SHA));
    }

    @Test
    void testGetUserRepositories_nonExistingUser() throws Exception {
        when(githubClient.fetchUserRepositories(INVALID_USERNAME))
                .thenThrow(HttpClientErrorException.create(
                        HttpStatus.NOT_FOUND,
                        NOT_FOUND_MESSAGE,
                        HttpHeaders.EMPTY,
                        new byte[0],
                        StandardCharsets.UTF_8
                ));

        mockMvc.perform(get(URI_TEMPLATE, INVALID_USERNAME))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(NOT_FOUND_STATUS))
                .andExpect(jsonPath("$.message").value(USER_NOT_FOUND_MESSAGE));
    }

    private Repository createRepository(String repoName, String ownerLogin, boolean fork) {
        return new Repository(repoName, new Owner(ownerLogin), fork);
    }

    private Branch createBranch(String branchName, String commitSha) {
        return new Branch(branchName, new Commit(commitSha));
    }

    private List<Repository> createRepositories() {
        Repository nonForkRepo = createRepository(NON_FORKED_REPO, VALID_USERNAME, false);
        Repository forkRepo = createRepository(FORKED_REPO, VALID_USERNAME, true);
        return List.of(nonForkRepo, forkRepo);
    }

    private List<Branch> createBranches() {
        Branch branch = createBranch(BRANCH_MASTER, COMMIT_SHA);
        return List.of(branch);
    }
}