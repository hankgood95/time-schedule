package com.user.user_service.service;

import com.user.user_service.exception.LoginException;
import com.user.user_service.model.dao.LoginDao;
import com.user.user_service.model.domain.User;
import com.user.user_service.model.domain.UserStatus;
import com.user.user_service.model.dto.request.LoginRequest;
import com.user.user_service.model.dto.response.JwtResponse;
import com.user.user_service.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 로그인 서비스 구현체
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(LoginRequest request) {
        // 1. 이메일로 사용자 조회
        User user = loginDao.findByEmail(request.getEmail());
        if (user == null) {
            throw new LoginException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new LoginException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 3. 사용자 상태 확인 (활성화된 사용자인지)
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new LoginException("비활성화된 계정입니다.");
        }

        // 4. JWT 토큰 생성
        List<String> roles = user.isAdmin() ? 
            List.of("USER", "ADMIN") : List.of("USER");
        
        String token = jwtTokenProvider.createToken(user.getUserId().toString(), roles);

        // 5. JWT 응답 반환
        return new JwtResponse(token);
    }
} 