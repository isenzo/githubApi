package com.jurkiewicz.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class ResponseException extends WebApplicationException {
    private final int statusCode;

    public ResponseException(int statusCode, String message) {
        super(Response.status(statusCode).entity(new ErrorResponse(statusCode, message)).build());
        this.statusCode = statusCode;
    }
}
