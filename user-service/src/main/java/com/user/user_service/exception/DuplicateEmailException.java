package com.user.user_service.exception;

/**
 * 이메일 중복 시 발생하는 예외
 */
public class DuplicateEmailException extends RuntimeException {
    
    public DuplicateEmailException(String message) {
        super(message);
    }
    
    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }
} 