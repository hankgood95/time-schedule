package com.user.user_service.config.swagger;

import com.user.user_service.model.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 로그인 API 전용 응답 어노테이션
 * 로그인에 특화된 응답 코드들만 포함
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", 
        description = "로그인 성공",
        content = @Content(
            schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(
                    name = "로그인 성공",
                    summary = "로그인 성공 시 응답 (토큰 포함)",
                    value = """
                    {
                      "success": true,
                      "message": "로그인 성공",
                      "data": {
                        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                      }
                    }
                    """
                )
            }
        )
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400", 
        description = "잘못된 요청 (이메일/비밀번호 형식 오류)",
        content = @Content(
            schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(
                    name = "입력값 오류",
                    summary = "이메일 또는 비밀번호 형식이 잘못됨",
                    value = """
                    {
                      "success": false,
                      "message": "이메일 형식이 올바르지 않습니다",
                      "data": null
                    }
                    """
                )
            }
        )
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "401", 
        description = "로그인 실패 (이메일/비밀번호 불일치)",
        content = @Content(
            schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(
                    name = "인증 실패",
                    summary = "이메일 또는 비밀번호가 일치하지 않음",
                    value = """
                    {
                      "success": false,
                      "message": "이메일 또는 비밀번호가 올바르지 않습니다",
                      "data": null
                    }
                    """
                )
            }
        )
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500", 
        description = "서버 내부 오류",
        content = @Content(
            schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(
                    name = "서버 오류",
                    summary = "서버 내부 오류 발생",
                    value = """
                    {
                      "success": false,
                      "message": "로그인 처리 중 오류가 발생했습니다",
                      "data": null
                    }
                    """
                )
            }
        )
    )
})
public @interface LoginApiResponses {
} 