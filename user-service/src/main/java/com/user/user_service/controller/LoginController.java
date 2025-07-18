package com.user.user_service.controller;

import com.user.user_service.config.swagger.LoginApiResponses;
import com.user.user_service.model.dto.request.LoginRequest;
import com.user.user_service.model.dto.response.ApiResponse;
import com.user.user_service.model.dto.response.JwtResponse;
import com.user.user_service.model.dto.response.UserInfoResponse;
import com.user.user_service.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

/**
 * 로그인 관련 API를 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "로그인", description = "로그인 관련 API")
@LoginApiResponses  // 로그인 전용 응답만 표시
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인 및 JWT 토큰 발급")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = loginService.login(loginRequest);
        return ResponseEntity.ok()
                .body(ApiResponse.success("로그인 성공", jwtResponse));
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "JWT 토큰을 사용하여 현재 로그인된 사용자 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        UserInfoResponse userInfo = loginService.getUserInfo(token);
        return ResponseEntity.ok(ApiResponse.success("사용자 정보 조회 성공", userInfo));
    }
} 