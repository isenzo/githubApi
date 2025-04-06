package com.jurkiewicz.githubapi.controller;

import com.jurkiewicz.githubapi.model.ApiErrorResponse;
import com.jurkiewicz.githubapi.model.RepositoryResponseDto;
import com.jurkiewicz.githubapi.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService githubService;

    private static final int NOT_FOUND_STATUS = 404;
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RepositoryResponseDto>> getUserRepositories(@PathVariable String username) {
        return ResponseEntity.ok(githubService.fetchUserRepositories(username));
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFoundException(HttpClientErrorException.NotFound ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(NOT_FOUND_STATUS, USER_NOT_FOUND_MESSAGE));
    }
}
