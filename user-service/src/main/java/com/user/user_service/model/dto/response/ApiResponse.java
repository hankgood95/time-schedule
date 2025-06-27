package com.user.user_service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * API 응답을 표준화하기 위한 공통 응답 클래스
 * 
 * @param <T> 응답 데이터의 타입
 */
@Getter
@Schema(description = "API 응답 래퍼", 
       example = """
       {
         "success": true,
         "message": "요청이 성공적으로 처리되었습니다",
         "data": {}
       }
       """)
public class ApiResponse<T> {
    @Schema(description = "요청 성공 여부", example = "true")
    private final boolean success;
    
    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다")
    private final String message;
    
    @Schema(description = "응답 데이터")
    private final T data;

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * 성공 응답 생성
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    /**
     * 성공 응답 생성 (메시지 포함)
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * 실패 응답 생성
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
} 