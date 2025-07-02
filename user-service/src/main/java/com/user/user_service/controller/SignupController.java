package com.user.user_service.controller;

import com.user.user_service.config.swagger.SignupApiResponses;
import com.user.user_service.model.dto.request.SignUpRequest;
import com.user.user_service.model.dto.response.ApiResponse;
import com.user.user_service.model.dto.response.SignupResponse;
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
 * - 회원가입: 새로운 사용자 등록 및 JWT 토큰 발급
 * - (향후 추가될 기능들: 사용자 정보 조회, 수정, 삭제 등)
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
     * @return 회원가입 성공 시 201 Created 응답 (토큰, 이름, 홈화면 이동 메시지 포함)
     */
    @PostMapping("/signup")
    @Operation(
        summary = "회원가입", 
        description = "새로운 사용자를 등록하고 JWT 토큰을 발급합니다. " +
                     "회원가입 성공 시 즉시 로그인 상태가 되어 홈화면으로 이동할 수 있습니다."
    )
    public ResponseEntity<ApiResponse<SignupResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        // 1. SignupService를 통해 회원가입 처리 및 JWT 토큰 생성
        SignupResponse signupResponse = signupService.signUp(request);
        
        // 2. HTTP 201 Created 상태로 성공 응답 반환
        // 201 Created는 리소스 생성 성공을 의미하는 표준 HTTP 상태 코드
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입이 완료되었습니다.", signupResponse));
    }
} 