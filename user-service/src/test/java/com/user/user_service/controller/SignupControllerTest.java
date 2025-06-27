package com.user.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.user_service.exception.DuplicateEmailException;
import com.user.user_service.model.dto.request.SignUpRequest;
import com.user.user_service.service.SignupService;
import com.user.user_service.config.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SignupController의 단위 테스트 클래스
 * 
 * @WebMvcTest: 
 * - 웹 계층만 테스트하기 위한 어노테이션
 * - @Controller, @ControllerAdvice, @JsonComponent, @Filter, WebMvcConfigurer, HandlerMethodArgumentResolver만 스캔
 * - SecurityConfig를 포함하여 Spring Security 설정도 함께 테스트
 */
@WebMvcTest(controllers = SignupController.class, 
    includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class SignupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignupService signupService;

    @Test
    @DisplayName("정상적인 회원가입 요청")
    void signUp_Success() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password123!");
        request.setName("홍길동");

        doNothing().when(signupService).signUp(any(SignUpRequest.class));

        mockMvc.perform(post("/time-schedule/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."));
    }

    @Test
    @DisplayName("이메일 중복 시 회원가입 실패")
    void signUp_DuplicateEmail() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password123!");
        request.setName("홍길동");

        doThrow(new DuplicateEmailException("이미 사용 중인 이메일입니다."))
                .when(signupService).signUp(any(SignUpRequest.class));

        mockMvc.perform(post("/time-schedule/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("이미 사용 중인 이메일입니다."));
    }

    @Test
    @DisplayName("유효성 검사 실패 시 회원가입 실패")
    void signUp_ValidationFailed() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("invalid-email");
        request.setPassword("123");
        request.setName("홍");

        mockMvc.perform(post("/time-schedule/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("입력값이 올바르지 않습니다."));
    }
} 