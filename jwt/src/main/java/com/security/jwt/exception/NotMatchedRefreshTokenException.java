package com.security.jwt.exception;

public class NotMatchedRefreshTokenException extends RuntimeException {
    public NotMatchedRefreshTokenException(String message) {
        super(message);
    }
}
