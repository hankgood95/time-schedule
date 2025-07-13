package com.user.user_service.service;

import com.user.user_service.model.dto.response.UserInfoResponse;

/**
 * 사용자 정보 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 */
public interface UserService {
    
    /**
     * 사용자 ID를 통해 사용자 정보를 조회
     * 
     * @param userId 사용자 ID
     * @return 사용자 정보 응답 DTO
     */
    UserInfoResponse getUserInfoById(String userId);
} 