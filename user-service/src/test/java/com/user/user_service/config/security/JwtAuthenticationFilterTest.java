package com.user.user_service.config.security;

import com.user.user_service.util.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * JwtAuthenticationFilter의 동작을 검증하는 단위 테스트 클래스입니다.
 * 
 * 주요 테스트 시나리오:
 * 1. 유효한 JWT 토큰이 있을 때 SecurityContext에 인증 정보가 정상적으로 설정되는지 검증
 * 2. Authorization 헤더가 없거나, Bearer 접두사가 없거나, 토큰이 유효하지 않을 때 인증 정보가 설정되지 않는지 검증
 * 3. JWT 토큰 처리 중 예외가 발생해도 필터 체인이 중단되지 않고 계속 진행되는지 검증
 *
 * 각 테스트는 SecurityContext를 초기화하여 테스트 간 상태가 공유되지 않도록 합니다.
 */
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 각 테스트 실행 전 JwtAuthenticationFilter 인스턴스를 생성하고
     * SecurityContext를 초기화합니다.
     */
    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);
        SecurityContextHolder.clearContext();
    }

    /**
     * [성공 케이스]
     * 유효한 JWT 토큰이 Authorization 헤더에 포함되어 있을 때
     * SecurityContext에 인증 정보가 정상적으로 설정되는지 검증합니다.
     *
     * 준비: MockJwtTokenProvider가 토큰 유효성, 사용자 ID, 권한을 반환하도록 설정
     * 실행: doFilterInternal 호출
     * 검증: SecurityContext에 인증 객체가 존재하고, 사용자명/권한이 올바른지 확인
     */
    @Test
    @DisplayName("유효한 JWT 토큰이 있을 때 인증 정보가 설정된다")
    void doFilterInternal_ValidToken_SetsAuthentication() throws Exception {
        // given: Mock 요청/응답/필터체인 및 유효한 토큰 준비
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        request.addHeader("Authorization", "Bearer " + validToken);

        // JwtTokenProvider가 유효한 토큰임을 반환하도록 Mock 설정
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(validToken)).thenReturn("test@example.com");
        when(jwtTokenProvider.getRolesFromToken(validToken)).thenReturn(List.of("USER"));

        // when: 필터 실행
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then: SecurityContext에 인증 정보가 정상적으로 설정되었는지 검증
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("test@example.com");
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));
    }

    /**
     * [실패 케이스]
     * Authorization 헤더가 없는 경우 인증 정보가 설정되지 않아야 합니다.
     */
    @Test
    @DisplayName("Authorization 헤더가 없을 때 인증 정보가 설정되지 않는다")
    void doFilterInternal_NoAuthorizationHeader_NoAuthenticationSet() throws Exception {
        // given: Authorization 헤더가 없는 요청
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        // when: 필터 실행
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then: 인증 정보가 없어야 함
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    /**
     * [실패 케이스]
     * Authorization 헤더에 Bearer 접두사가 없으면 인증 정보가 설정되지 않아야 합니다.
     */
    @Test
    @DisplayName("Bearer 접두사가 없는 토큰은 무시된다")
    void doFilterInternal_NoBearerPrefix_NoAuthenticationSet() throws Exception {
        // given: Bearer 접두사가 없는 Authorization 헤더
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        request.addHeader("Authorization", "InvalidToken");

        // when: 필터 실행
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then: 인증 정보가 없어야 함
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    /**
     * [실패 케이스]
     * 유효하지 않은 JWT 토큰이 전달되면 인증 정보가 설정되지 않아야 합니다.
     */
    @Test
    @DisplayName("유효하지 않은 JWT 토큰은 예외를 발생시킨다")
    void doFilterInternal_InvalidToken_ThrowsException() throws Exception {
        // given: 유효하지 않은 토큰이 포함된 요청
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        String invalidToken = "invalid.token.here";
        request.addHeader("Authorization", "Bearer " + invalidToken);

        // JwtTokenProvider가 false를 반환하도록 Mock 설정
        when(jwtTokenProvider.validateToken(invalidToken)).thenReturn(false);

        // when & then: InvalidTokenException 발생을 expect
        org.junit.jupiter.api.Assertions.assertThrows(
            com.user.user_service.exception.InvalidTokenException.class,
            () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain)
        );
    }

    /**
     * [예외 케이스]
     * JWT 토큰 처리 중 예외가 발생해도 필터 체인은 중단되지 않고 계속 진행되어야 합니다.
     * (즉, 예외 발생 시에도 응답이 정상적으로 반환되어야 함)
     */
    @Test
    @DisplayName("JWT 토큰 처리 중 예외가 발생해도 필터 체인은 계속 진행된다")
    void doFilterInternal_ExceptionOccurs_ContinuesFilterChain() throws Exception {
        // given: 예외를 발생시키는 JwtTokenProvider Mock 설정
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenProvider.validateToken(anyString())).thenThrow(new RuntimeException("Token validation error"));

        // when & then: 예외가 발생해도 필터 체인이 중단되지 않아야 함
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // then: 필터 체인이 정상적으로 실행되었는지 확인 (응답 상태 200)
        assertThat(response.getStatus()).isEqualTo(200);
    }
} 