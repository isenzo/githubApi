package com.jurkiewicz.facade;

import com.jurkiewicz.model.GithubRepoResponse;
import com.jurkiewicz.facade.support.ValidationSupport;
import com.jurkiewicz.service.GithubService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class GithubFacade {

    private final GithubService githubService;
    private final ValidationSupport validationSupport;

    public Uni<List<GithubRepoResponse>> getUserRepositories(String username) {
        validationSupport.validateUsername(username);
        return githubService.getUserRepositories(username);
    }
}
