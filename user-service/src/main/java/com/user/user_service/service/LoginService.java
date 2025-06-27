package com.user.user_service.service;

import com.user.user_service.model.dto.request.LoginRequest;
import com.user.user_service.model.dto.response.JwtResponse;

public interface LoginService {
    /**
     * 로그인 처리
     * @param request 로그인 요청 DTO
     * @return 로그인 성공 시 토큰 또는 사용자 정보 등 반환 예정
     */
    JwtResponse login(LoginRequest request);
} 