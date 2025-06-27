package com.user.user_service.controller;

import com.user.user_service.config.swagger.SignupApiResponses;
import com.user.user_service.model.dto.request.SignUpRequest;
import com.user.user_service.model.dto.response.ApiResponse;
import com.user.user_service.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원가입 관련 API를 처리하는 컨트롤러
 * 
 * 주요 기능:
 * - 회원가입
 * - (향후 추가될 기능들: 로그인, 사용자 정보 조회, 수정, 삭제 등)
 */
@RestController
@RequestMapping("/time-schedule")
@RequiredArgsConstructor
@Tag(name = "회원가입", description = "회원가입 관련 API")
@SignupApiResponses  // 회원가입 전용 응답만 표시
public class SignupController {

    private final SignupService signupService;

    /**
     * 회원가입 API
     * 
     * @param request 회원가입 요청 정보 (이메일, 비밀번호, 이름)
     * @return 회원가입 성공 시 201 Created 응답
     * 
     * API 설명:
     * - POST /api/v1/users/signup
     * - 요청 본문: { "email": "user@example.com", "password": "password123!", "name": "홍길동" }
     * - 성공 시: 201 Created
     * - 실패 시: 400 Bad Request (검증 실패) 또는 409 Conflict (이메일 중복)
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody SignUpRequest request) {
        signupService.signUp(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입이 완료되었습니다.", null));
    }
} 