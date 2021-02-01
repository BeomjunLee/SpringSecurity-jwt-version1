package com.security.jwt.exception;

public class NotFoundRefreshTokenException extends RuntimeException {
    public NotFoundRefreshTokenException(String message) {
        super(message);
    }
}
