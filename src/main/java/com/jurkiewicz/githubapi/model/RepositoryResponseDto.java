package com.jurkiewicz.githubapi.model;

import java.util.List;

public record RepositoryResponseDto(String repositoryName, String ownerLogin, List<Branch> branches) {
}
