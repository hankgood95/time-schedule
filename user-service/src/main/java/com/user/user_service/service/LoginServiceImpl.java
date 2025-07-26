package com.user.user_service.service;

import com.user.user_service.exception.LoginException;
import com.user.user_service.model.dao.LoginDao;
import com.user.user_service.model.domain.User;
import com.user.user_service.model.domain.UserStatus;
import com.user.user_service.model.dto.request.LoginRequest;
import com.user.user_service.model.dto.response.JwtResponse;
import com.user.user_service.model.dto.response.UserInfoResponse;
import com.user.user_service.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // ... 기존 로그인 로직 ...
        User user = loginDao.findByEmail(request.getEmail());
        if (user == null) {
            throw new LoginException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new LoginException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new LoginException("비활성화된 계정입니다.");
        }

        List<String> roles = user.isAdmin() ?
            List.of("USER", "ADMIN") : List.of("USER");

        String token = jwtTokenProvider.createToken(user.getUserId().toString(), roles);

        return new JwtResponse(token);
    }

    @Override
    public UserInfoResponse getUserInfo(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new LoginException("유효하지 않은 토큰입니다.");
        }

        String userIdStr = jwtTokenProvider.getUserIdFromToken(token);
        if (userIdStr == null) {
            throw new LoginException("토큰에서 사용자 정보를 찾을 수 없습니다.");
        }
        
        // LoginDao에 findById 추가 필요
        User user = loginDao.findById(Long.parseLong(userIdStr)); 
        if (user == null) {
            throw new LoginException("사용자를 찾을 수 없습니다.");
        }

        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .status(user.getStatus().toString())
                .build();
    }
}