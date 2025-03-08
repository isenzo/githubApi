package com.jurkiewicz.client;

import com.jurkiewicz.model.GithubBranch;
import com.jurkiewicz.model.GithubRepository;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "github-api")
public interface GithubClient {

    @GET
    @Path("/users/{username}/repos")
    @ClientHeaderParam(name = "Authorization", value = "Bearer ${quarkus.rest-client.github-api.headers.Authorization}")
    Uni<List<GithubRepository>> getUserRepositories(@PathParam("username") String username);

    @GET
    @Path("/repos/{username}/{repo}/branches")
    @ClientHeaderParam(name = "Authorization", value = "Bearer ${quarkus.rest-client.github-api.headers.Authorization}")
    Uni<List<GithubBranch>> getRepositoryBranches(@PathParam("username") String username, @PathParam("repo") String repoName);
}
