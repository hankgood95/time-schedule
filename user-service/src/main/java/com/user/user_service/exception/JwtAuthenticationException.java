package com.user.user_service.exception;

/**
 * JWT 토큰 인증 실패 시 발생하는 예외
 */
public class JwtAuthenticationException extends RuntimeException {
    
    public JwtAuthenticationException(String message) {
        super(message);
    }
    
    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
} 