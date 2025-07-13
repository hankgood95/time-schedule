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
 * 회원가입 API 전용 응답 어노테이션
 * 회원가입에 특화된 응답 코드들만 포함
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "201", 
        description = "회원가입 성공",
        content = @Content(
            schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(
                    name = "회원가입 성공",
                    summary = "회원가입 성공 시 응답",
                    value = """
                    {
                      "success": true,
                      "message": "회원가입이 완료되었습니다.",
                      "data": null
                    }
                    """
                )
            }
        )
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400", 
        description = "잘못된 요청 (유효성 검사 실패)",
        content = @Content(
            schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(
                    name = "유효성 검사 실패",
                    summary = "이메일, 비밀번호, 이름 형식 오류",
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
        responseCode = "409", 
        description = "이메일 중복",
        content = @Content(
            schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(
                    name = "이메일 중복",
                    summary = "이미 존재하는 이메일",
                    value = """
                    {
                      "success": false,
                      "message": "이미 존재하는 이메일입니다",
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
                    summary = "회원가입 처리 중 오류 발생",
                    value = """
                    {
                      "success": false,
                      "message": "회원가입 처리 중 오류가 발생했습니다",
                      "data": null
                    }
                    """
                )
            }
        )
    )
})
public @interface SignupApiResponses {
} 