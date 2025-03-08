package com.jurkiewicz.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class GithubRepository {

    private String name;
    private boolean fork;
    private Owner owner;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @RegisterForReflection
    public static class Owner {
        private String login;
    }
}
