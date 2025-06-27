package com.user.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.user_service.config.security.SecurityConfig;
import com.user.user_service.model.dto.request.LoginRequest;
import com.user.user_service.model.dto.response.JwtResponse;
import com.user.user_service.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * LoginController 단위 테스트
 */
@WebMvcTest(LoginController.class)
@Import(SecurityConfig.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginService loginService;

    private LoginRequest validLoginRequest;
    private LoginRequest invalidLoginRequest;
    private JwtResponse jwtResponse;

    @BeforeEach
    void setUp() {
        // 유효한 로그인 요청
        validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail("test@example.com");
        validLoginRequest.setPassword("password123");

        // 유효하지 않은 로그인 요청
        invalidLoginRequest = new LoginRequest();
        invalidLoginRequest.setEmail("invalid@example.com");
        invalidLoginRequest.setPassword("wrongpassword");

        // JWT 응답
        jwtResponse = new JwtResponse("eyJhbGciOiJIUzUxMiJ9.test.token");
    }

    @Test
    @DisplayName("정상적인 로그인 요청 시 JWT 토큰을 반환한다")
    void loginSuccess() throws Exception {
        // given
        when(loginService.login(any(LoginRequest.class))).thenReturn(jwtResponse);

        // when & then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.data.token").value("eyJhbGciOiJIUzUxMiJ9.test.token"));
    }

    @Test
    @DisplayName("이메일이 비어있으면 400 에러를 반환한다")
    void loginWithEmptyEmail() throws Exception {
        // given
        LoginRequest requestWithEmptyEmail = new LoginRequest();
        requestWithEmptyEmail.setEmail("");
        requestWithEmptyEmail.setPassword("password123");

        // when & then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithEmptyEmail)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호가 비어있으면 400 에러를 반환한다")
    void loginWithEmptyPassword() throws Exception {
        // given
        LoginRequest requestWithEmptyPassword = new LoginRequest();
        requestWithEmptyPassword.setEmail("test@example.com");
        requestWithEmptyPassword.setPassword("");

        // when & then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithEmptyPassword)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 이메일 형식이면 400 에러를 반환한다")
    void loginWithInvalidEmailFormat() throws Exception {
        // given
        LoginRequest requestWithInvalidEmail = new LoginRequest();
        requestWithInvalidEmail.setEmail("invalid-email");
        requestWithInvalidEmail.setPassword("password123");

        // when & then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithInvalidEmail)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 요청이 null이면 400 에러를 반환한다")
    void loginWithNullRequest() throws Exception {
        // when & then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
} 