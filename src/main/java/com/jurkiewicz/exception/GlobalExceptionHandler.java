package com.jurkiewicz.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    public Response toResponse(Throwable exception) {
        int statusCode = (exception instanceof ResponseException) ?
                ((ResponseException) exception).getStatusCode() :
                (exception instanceof DataViolationException) ?
                        ((DataViolationException) exception).getStatusCode() :
                        Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        return Response.status(statusCode)
                .entity(new ErrorResponse(statusCode, exception.getMessage()))
                .build();
    }
}
