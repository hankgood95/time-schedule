package com.user.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.user_service.config.security.SecurityConfig;
import com.user.user_service.model.dto.response.UserInfoResponse;
import com.user.user_service.service.UserService;
import com.user.user_service.util.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController 단위 테스트
 */
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private UserInfoResponse userInfoResponse;
    private String validToken;

    @BeforeEach
    void setUp() {
        userInfoResponse = new UserInfoResponse(
            1L,
            "test@example.com",
            "홍길동",
            "ACTIVE"
        );
        
        // 유효한 JWT 토큰 생성 (실제 토큰 형식)
        validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2MzQ1Njc4OTAsImV4cCI6MTYzNDY1NDI5MH0.test_signature";
    }

    @Test
    @DisplayName("유효한 JWT 토큰으로 내 정보 조회 시 성공")
    void getMyInfo_WithValidToken_Success() throws Exception {
        // given
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(validToken)).thenReturn("1");
        when(jwtTokenProvider.getRolesFromToken(validToken)).thenReturn(List.of("USER"));
        when(userService.getUserInfoById("1")).thenReturn(userInfoResponse);

        // when & then
        mockMvc.perform(get("/time-schedule/users/me")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("사용자 정보 조회 성공"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.name").value("홍길동"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("유효하지 않은 JWT 토큰으로 내 정보 조회 시 실패")
    void getMyInfo_WithInvalidToken_Unauthorized() throws Exception {
        // given
        String invalidToken = "invalid.token.here";
        when(jwtTokenProvider.validateToken(invalidToken)).thenReturn(false);

        // when & then
        org.junit.jupiter.api.Assertions.assertThrows(
            com.user.user_service.exception.InvalidTokenException.class,
            () -> mockMvc.perform(get("/time-schedule/users/me")
                .header("Authorization", "Bearer " + invalidToken)
                .contentType(MediaType.APPLICATION_JSON))
        );
    }

    @Test
    @DisplayName("토큰 없이 내 정보 조회 시 실패")
    void getMyInfo_WithoutToken_Unauthorized() throws Exception {
        // when & then
        mockMvc.perform(get("/time-schedule/users/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Bearer 접두사 없이 토큰 전송 시 실패")
    void getMyInfo_WithoutBearerPrefix_Unauthorized() throws Exception {
        // when & then
        mockMvc.perform(get("/time-schedule/users/me")
                .header("Authorization", validToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("유효한 토큰이지만 사용자를 찾을 수 없을 때 404 에러 반환")
    void getMyInfo_ValidTokenButUserNotFound() throws Exception {
        // given
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(validToken)).thenReturn("999");
        when(jwtTokenProvider.getRolesFromToken(validToken)).thenReturn(List.of("USER"));
        when(userService.getUserInfoById("999"))
                .thenThrow(new com.user.user_service.exception.UserNotFoundException("사용자를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/time-schedule/users/me")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."));
    }
} 