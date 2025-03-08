package com.jurkiewicz.service;

import com.jurkiewicz.client.GithubClient;
import com.jurkiewicz.model.GithubBranch;
import com.jurkiewicz.model.GithubRepoResponse;
import com.jurkiewicz.model.GithubRepository;
import com.jurkiewicz.exception.ResponseException;
import com.jurkiewicz.facade.support.ValidationSupport;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import java.util.List;
import java.util.stream.Collectors;

import static com.jurkiewicz.exception.message.ExceptionMessage.USER_NOT_FOUND;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class GithubService {

    @RestClient
    private GithubClient githubClient;

    private final ValidationSupport validationSupport;

    public Uni<List<GithubRepoResponse>> getUserRepositories(String username) {
        return githubClient.getUserRepositories(username)
                .onFailure(ClientWebApplicationException.class)
                .recoverWithUni(ex -> handleGithubNotFound(username, ex))
                .onItem().transformToUni(repos -> handleEmptyRepositoryList(username, repos))
                .onItem().transformToUni(repos -> fetchBranchesForRepositories(username, repos));
    }

    private Uni<List<GithubRepository>> handleEmptyRepositoryList(String username, List<GithubRepository> repositories) {
        validationSupport.validateUserHasRepositories(username, repositories);
        return Uni.createFrom().item(repositories);
    }

    private Uni<List<GithubRepoResponse>> fetchBranchesForRepositories(String username, List<GithubRepository> repositories) {
        validationSupport.validateUserHasRepositories(username, repositories);

        List<Uni<GithubRepoResponse>> responseList = repositories.stream()
                .filter(this::isNotFork)
                .map(repo -> fetchBranches(username, repo))
                .collect(Collectors.toList());

        return Uni.join().all(responseList).andCollectFailures();
    }

    private Uni<GithubRepoResponse> fetchBranches(String username, GithubRepository repository) {
        return githubClient.getRepositoryBranches(username, repository.getName())
                .map(branches -> validationSupport.validateAndCreateResponse(username, repository, branches));
    }

    private boolean isNotFork(GithubRepository repo) {
        return !repo.isFork();
    }

    private Uni<List<GithubRepository>> handleGithubNotFound(String username, Throwable ex) {
        if (ex instanceof ClientWebApplicationException clientEx && clientEx.getResponse().getStatus() == 404) {
            return Uni.createFrom().failure(new ResponseException(404, USER_NOT_FOUND.formatMessage(username)));
        }
        return Uni.createFrom().failure(ex);

    }
}

