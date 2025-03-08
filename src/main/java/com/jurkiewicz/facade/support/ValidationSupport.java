package com.jurkiewicz.facade.support;

import com.jurkiewicz.model.GithubBranch;
import com.jurkiewicz.model.GithubRepoResponse;
import com.jurkiewicz.model.GithubRepository;
import com.jurkiewicz.exception.DataViolationException;
import com.jurkiewicz.exception.ResponseException;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Objects;

import static com.jurkiewicz.exception.message.ExceptionMessage.*;

@ApplicationScoped
public class ValidationSupport {

    private static final String REGEX_USERNAME = "^[a-zA-Z0-9-]+$";

    public void validateUsername(String username) {
        usernameIsEmpty(username);
        usernameIsNotCorrect(username);
    }

    public GithubRepoResponse validateAndCreateResponse(String username, GithubRepository repository, List<GithubBranch> branches) {
        validateBranchesNotEmpty(username, branches);
        String ownerLogin = validateUserIsNullSafe(repository).getLogin();
        return new GithubRepoResponse(repository.getName(), ownerLogin, branches);
    }

    private void usernameIsEmpty(String username) {
        if (Objects.isNull(username) || username.trim().isEmpty()) {
            throw new DataViolationException(INVALID_USERNAME.getMessage());
        }
    }

    public void validateBranchesNotEmpty(String username, List<GithubBranch> branches) {
        if (branches.isEmpty()) {
            throw new ResponseException(404, NO_BRANCHES_FOUND.formatMessage(username));
        }
    }

    public void validateUserHasRepositories(String username, List<GithubRepository> repositories) {
        if (Objects.isNull(repositories) || repositories.isEmpty()) {
            throw new ResponseException(404, USER_NOT_FOUND.formatMessage(username));
        }
    }

    private void usernameIsNotCorrect(String username) {
        if (!username.matches(REGEX_USERNAME)) {
            throw new DataViolationException(INCORRECT_USERNAME.formatMessage(username));
        }
    }

    public GithubRepository.Owner validateUserIsNullSafe(GithubRepository repository) {
        if (Objects.isNull(repository) || Objects.isNull(repository.getOwner())) {
            throw new ResponseException(400, REPOSITORY_OWNER_NOT_FOUND.getMessage());
        }
        return repository.getOwner();
    }
}
