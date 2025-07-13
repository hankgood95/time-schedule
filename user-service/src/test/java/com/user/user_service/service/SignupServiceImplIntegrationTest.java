package com.user.user_service.service;

import com.user.user_service.exception.DuplicateEmailException;
import com.user.user_service.model.dto.request.SignUpRequest;
import com.user.user_service.model.dao.SignupDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class SignupServiceImplIntegrationTest {

    @Autowired
    private SignupService signupService;

    @Autowired
    private SignupDao signupDao;

    @Test
    @DisplayName("정상적으로 회원가입이 되고 DB에 저장된다")
    void signUp_success() {
        // given
        SignUpRequest request = new SignUpRequest();
        request.setEmail("test2@example.com");
        request.setPassword("Password123!");
        request.setName("홍길동");

        // when
        signupService.signUp(request);

        // then
        boolean exists = signupDao.existsByEmail("test2@example.com");
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("이메일 중복 시 DuplicateEmailException이 발생한다")
    void signUp_duplicateEmail() {
        // given
        SignUpRequest request = new SignUpRequest();
        request.setEmail("test3@example.com");
        request.setPassword("Password123!");
        request.setName("홍길동");

        // 먼저 한 번 저장
        signupService.signUp(request);

        // when & then
        assertThatThrownBy(() -> signupService.signUp(request))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("이미 사용 중인 이메일입니다.");
    }
} 