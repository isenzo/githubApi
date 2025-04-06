package com.jurkiewicz.githubapi.service;

import com.jurkiewicz.githubapi.client.GitHubClient;
import com.jurkiewicz.githubapi.model.Branch;
import com.jurkiewicz.githubapi.model.Repository;
import com.jurkiewicz.githubapi.model.RepositoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private final GitHubClient githubClient;

    public List<RepositoryResponseDto> fetchUserRepositories(String username) {
        List<Repository> repositories = githubClient.fetchUserRepositories(username);
        return repositories.stream()
                .filter(repository -> !repository.fork())
                .map(repository -> mapToRepositoryResponse(repository, username))
                .collect(Collectors.toList());
    }

    private RepositoryResponseDto mapToRepositoryResponse(Repository repository, String username) {
        List<Branch> branches = githubClient.fetchRepositoryBranches(username, repository.name());
        return new RepositoryResponseDto(repository.name(), repository.owner().login(), branches);
    }
}