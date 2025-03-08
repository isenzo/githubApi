package com.jurkiewicz.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {
    USER_NOT_FOUND("User %s not found."),
    USER_HAS_NO_PUBLIC_REPOSITORIES("User %s has no public repositories."),
    NO_BRANCHES_FOUND("User %s has no non-fork branches."),
    INVALID_USERNAME("Username cannot be null or empty."),
    INCORRECT_USERNAME("Username %s contains invalid characters. Only letters, numbers, and dashes are allowed."),
    REPOSITORY_OWNER_NOT_FOUND("Repository owner is missing.");

    private final String message;

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
