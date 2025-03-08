package com.jurkiewicz.resource;

import com.jurkiewicz.model.GithubRepoResponse;
import com.jurkiewicz.facade.GithubFacade;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Path("/github")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Getter
@RequiredArgsConstructor
public class GithubResource {

    private final GithubFacade githubFacade;

    @GET
    @Path("/{username}")
    public Uni<List<GithubRepoResponse>> getUserRepositories(@PathParam("username") String username) {
        return githubFacade.getUserRepositories(username);
    }
}
