package com.jurkiewicz.exception;

import lombok.Getter;

@Getter
public class DataViolationException extends RuntimeException {
    private final int statusCode;

    public DataViolationException(String message) {
        super(message);
        this.statusCode = 400;
    }
}
