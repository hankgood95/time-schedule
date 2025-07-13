package com.user.user_service.service;

import com.user.user_service.exception.LoginException;
import com.user.user_service.model.dao.LoginDao;
import com.user.user_service.model.dao.SignupDao;
import com.user.user_service.model.domain.User;
import com.user.user_service.model.domain.UserStatus;
import com.user.user_service.model.dto.request.LoginRequest;
import com.user.user_service.model.dto.request.SignUpRequest;
import com.user.user_service.model.dto.response.JwtResponse;
import com.user.user_service.util.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LoginServiceImpl 통합 테스트
 * 실제 DB를 사용하여 회원가입 후 로그인을 테스트
 */
@SpringBootTest
@Transactional
@ActiveProfiles("dev")
class LoginServiceImplIntegrationTest {

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private SignupServiceImpl signupService;

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private SignupDao signupDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private SignUpRequest signUpRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        // 회원가입 요청 데이터
        signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("integration@test.com");
        signUpRequest.setPassword("password123");
        signUpRequest.setName("통합테스트사용자");

        // 로그인 요청 데이터
        loginRequest = new LoginRequest();
        loginRequest.setEmail("integration@test.com");
        loginRequest.setPassword("password123");
    }

    @Test
    @DisplayName("회원가입 후 로그인이 정상적으로 동작한다")
    void signupAndLoginSuccess() {
        // given - 회원가입
        signupService.signUp(signUpRequest);

        // when - 로그인
        JwtResponse jwtResponse = loginService.login(loginRequest);

        // then - 로그인 성공 검증
        assertNotNull(jwtResponse);
        assertNotNull(jwtResponse.getToken());
        assertFalse(jwtResponse.getToken().isEmpty());

        // JWT 토큰 검증
        assertTrue(jwtTokenProvider.validateToken(jwtResponse.getToken()));
        String userId = jwtTokenProvider.getUserIdFromToken(jwtResponse.getToken());
        assertNotNull(userId);
    }

    @Test
    @DisplayName("회원가입하지 않은 사용자로 로그인 시 실패한다")
    void loginWithNonExistentUser() {
        // given - 회원가입하지 않은 상태

        // when & then - 로그인 실패
        LoginException exception = assertThrows(LoginException.class, () -> {
            loginService.login(loginRequest);
        });
        assertEquals("이메일 또는 비밀번호가 올바르지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시 실패한다")
    void loginWithWrongPassword() {
        // given - 회원가입
        signupService.signUp(signUpRequest);

        // 잘못된 비밀번호로 로그인 시도
        LoginRequest wrongPasswordRequest = new LoginRequest();
        wrongPasswordRequest.setEmail("integration@test.com");
        wrongPasswordRequest.setPassword("wrongpassword");

        // when & then - 로그인 실패
        LoginException exception = assertThrows(LoginException.class, () -> {
            loginService.login(wrongPasswordRequest);
        });
        assertEquals("이메일 또는 비밀번호가 올바르지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("DB에서 직접 사용자 조회가 가능하다")
    void findByEmailFromDatabase() {
        // given - 회원가입
        signupService.signUp(signUpRequest);

        // when - DB에서 직접 사용자 조회
        User foundUser = loginDao.findByEmail("integration@test.com");

        // then - 사용자 정보 검증
        assertNotNull(foundUser);
        assertEquals("integration@test.com", foundUser.getEmail());
        assertEquals("통합테스트사용자", foundUser.getName());
        assertEquals(UserStatus.ACTIVE, foundUser.getStatus());
        assertTrue(passwordEncoder.matches("password123!", foundUser.getPasswordHash()));
    }

    @Test
    @DisplayName("JWT 토큰에서 사용자 정보를 추출할 수 있다")
    void extractUserInfoFromJwtToken() {
        // given - 회원가입 후 로그인
        signupService.signUp(signUpRequest);
        JwtResponse jwtResponse = loginService.login(loginRequest);

        // when - JWT 토큰에서 정보 추출
        String userId = jwtTokenProvider.getUserIdFromToken(jwtResponse.getToken());
        var roles = jwtTokenProvider.getRolesFromToken(jwtResponse.getToken());

        // then - 추출된 정보 검증
        assertNotNull(userId);
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
        assertEquals("USER", roles.get(0));
    }
} 