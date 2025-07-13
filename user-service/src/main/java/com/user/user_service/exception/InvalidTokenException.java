package com.user.user_service.exception;

/**
 * 유효하지 않은 JWT 토큰 시 발생하는 예외
 */
public class InvalidTokenException extends RuntimeException {
    
    public InvalidTokenException(String message) {
        super(message);
    }
    
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
} 