package com.user.user_service.service;

import com.user.user_service.model.dto.request.LoginRequest;
import com.user.user_service.model.dto.response.JwtResponse;
import com.user.user_service.model.dto.response.UserInfoResponse;

public interface LoginService {
    /**
     * 로그인 처리
     * @param request 로그인 요청 DTO
     * @return 로그인 성공 시 토큰 또는 사용자 정보 등 반환 예정
     */
    JwtResponse login(LoginRequest request);

    /**
     * 토큰으로 사용자 정보 조회
     * @param token JWT 토큰
     * @return 사용자 정보
     */
    UserInfoResponse getUserInfo(String token);
}