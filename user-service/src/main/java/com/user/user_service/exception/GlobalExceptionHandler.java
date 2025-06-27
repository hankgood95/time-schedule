package com.user.user_service.exception;

import com.user.user_service.model.dto.response.ApiResponse;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 클래스
 * 
 * 애플리케이션에서 발생하는 예외들을 일관된 형식으로 처리하고 응답합니다.
 * 
 * 주요 어노테이션 설명:
 * 1. @RestControllerAdvice
 *    - @ControllerAdvice와 @ResponseBody가 결합된 어노테이션
 *    - 모든 @RestController에서 발생하는 예외를 처리
 *    - @ResponseBody가 포함되어 있어 응답을 자동으로 JSON으로 변환
 *    - 전역적으로 예외 처리를 중앙화하여 일관된 에러 응답 제공
 * 
 * 2. @ExceptionHandler
 *    - 특정 예외를 처리할 메서드를 지정
 *    - 여러 예외를 배열로 지정 가능: @ExceptionHandler({Exception1.class, Exception2.class})
 *    - 예외 발생 시 해당 메서드가 자동으로 호출되어 예외 처리
 *    - 메서드의 파라미터로 처리할 예외 타입을 지정
 */
//Hidden 어노테이션은 Swagger에서 해당 메서드를 숨깁니다. @RestControllerAdvice 이 swagger랑 충돌되서 사용용
@Hidden 
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 이메일 중복 예외 처리
     *
     * @param e DuplicateEmailException
     * @return 409 CONFLICT, 에러 메시지 포함
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateEmailException(DuplicateEmailException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(e.getMessage()));
    }

    /**
     * Validation 예외 처리
     * 
     * @param e MethodArgumentNotValidException
     * @return 400 BAD REQUEST, 검증 실패 필드와 에러 메시지를 포함한 응답
     *
     * MethodArgumentNotValidException:
     * - @Valid 어노테이션이 붙은 객체의 검증에 실패했을 때 발생
     * - 주로 DTO 객체의 필드 검증 실패 시 발생
     * - BindingResult에 모든 검증 에러 정보가 포함됨
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("입력값이 올바르지 않습니다."));
    }

    /**
     * 기타 예외 처리
     *
     * @param e Exception
     * @return 500 INTERNAL SERVER ERROR, 서버 오류 메시지 포함
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("서버 오류가 발생했습니다."));
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ApiResponse<String>> handleLoginException(LoginException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(e.getMessage()));
    }
} 