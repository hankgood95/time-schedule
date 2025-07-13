package com.user.user_service.service;

import com.user.user_service.exception.DuplicateEmailException;
import com.user.user_service.model.domain.User;
import com.user.user_service.model.domain.UserStatus;
import com.user.user_service.model.dao.SignupDao;
import com.user.user_service.model.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SignupService 인터페이스의 구현체
 * 
 * 주요 기능:
 * - 회원가입 처리
 * - 이메일 중복 체크
 * - 비밀번호 암호화
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignupServiceImpl implements SignupService {

    private final SignupDao signupDao;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리
     * 
     * @param request 회원가입 요청 정보
     * @throws DuplicateEmailException 이메일이 이미 존재하는 경우
     */
    @Override
    @Transactional
    public void signUp(SignUpRequest request) {
        // 이메일 중복 검사
        if (signupDao.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // User 엔티티 생성
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(encodedPassword)
                .name(request.getName())
                .status(UserStatus.ACTIVE)
                .build();

        // 사용자 저장
        signupDao.save(user);
    }
} 