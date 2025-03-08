package com.jurkiewicz.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class GithubRepoResponse {

    private String repositoryName;
    private String ownerLogin;
    private List<GithubBranch> branches;
}
