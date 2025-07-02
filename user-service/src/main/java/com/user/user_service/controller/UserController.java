package com.user.user_service.controller;

import com.user.user_service.model.dto.response.ApiResponse;
import com.user.user_service.model.dto.response.UserInfoResponse;
import com.user.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 정보 관련 API를 처리하는 컨트롤러
 * 
 * 주요 기능:
 * - 사용자 정보 조회 (JWT 토큰 기반 인증 필요)
 */
@RestController
@RequestMapping("/time-schedule/users")
@RequiredArgsConstructor
@Tag(name = "사용자 정보", description = "사용자 정보 관련 API")
public class UserController {

    private final UserService userService;

    /**
     * 현재 로그인한 사용자의 정보를 조회하는 API
     * 
     * JWT 토큰의 유효성을 검증하고, 토큰에서 추출한 사용자 정보를 반환합니다.
     * 
     * @return 현재 로그인한 사용자의 정보
     */
    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo() {
        // SecurityContext에서 현재 인증된 사용자 정보 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        
        // UserService를 통해 사용자 정보 조회
        UserInfoResponse userInfo = userService.getUserInfoById(userId);
        
        return ResponseEntity.ok()
                .body(ApiResponse.success("사용자 정보 조회 성공", userInfo));
    }
} 