package com.user.user_service.exception;

/**
 * 로그인 관련 예외 클래스
 */
public class LoginException extends RuntimeException {
    
    public LoginException(String message) {
        super(message);
    }
    
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
} 