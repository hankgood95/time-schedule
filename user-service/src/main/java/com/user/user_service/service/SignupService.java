package com.user.user_service.service;

import com.user.user_service.model.dto.request.SignUpRequest;
import com.user.user_service.model.dto.response.SignupResponse;

/**
 * 회원가입 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 * 
 * 주요 기능:
 * - 회원가입
 * - (향후 추가될 기능들: 로그인, 사용자 정보 조회, 수정, 삭제 등)
 */
public interface SignupService {
    
    /**
     * 회원가입 처리
     * 
     * @param request 회원가입 요청 정보 (이메일, 비밀번호, 이름)
     * @return 회원가입 성공 응답 (토큰, 이름, 홈화면 이동 메시지)
     * @throws DuplicateEmailException 이메일이 이미 존재하는 경우
     */
    SignupResponse signUp(SignUpRequest request);
} 