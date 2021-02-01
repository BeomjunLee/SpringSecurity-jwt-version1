package com.security.jwt.exception;

public class NewAccessTokenIssuedException extends RuntimeException {
    public NewAccessTokenIssuedException(String message) {
        super(message);
    }
}
