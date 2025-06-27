package com.user.user_service.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security 설정
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * 비밀번호 암호화를 위한 PasswordEncoder 빈 등록
     * BCrypt 알고리즘 사용
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * SecurityFilterChain 설정
     * 회원가입 엔드포인트는 인증 없이 접근 가능하도록 설정
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/time-schedule/signup").permitAll()
                .requestMatchers("/auth/login").permitAll()
                
                // Swagger UI 관련 경로 허용
                // 현재는 모든 사용자가 접근 가능 (개발용)
                // 실무에서는 아래 방법들 중 하나로 제한하는 것을 권장:
                //
                // 방법 1: 특정 IP만 허용 (가장 일반적)
                // .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                //     .hasIpAddress("192.168.1.100")  // 특정 개발자 IP
                //     .hasIpAddress("192.168.1.0/24") // IP 대역 허용
                //
                // 방법 2: 관리자 권한만 허용
                // .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                //     .hasRole("ADMIN")
                //
                // 방법 3: 프로필별 제어 (application-dev.properties에서만 활성화)
                // .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                //     .access(new WebExpressionAuthorizationManager("'${spring.profiles.active}' == 'dev'"))
                //
                // 방법 4: 완전 비활성화 (운영환경용)
                // .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").denyAll()
                //
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/v3/api-docs/**", "/v3/api-docs").permitAll()
                
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
} 