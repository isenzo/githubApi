package com.jurkiewicz.githubapi.model;

public record Repository(String name, Owner owner, boolean fork) {
}