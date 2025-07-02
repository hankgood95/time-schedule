package com.user.user_service.config.security;

import com.user.user_service.exception.InvalidTokenException;
import com.user.user_service.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT 토큰 인증을 처리하는 필터
 * 
 * 주요 기능:
 * - 요청 헤더에서 JWT 토큰 추출
 * - 토큰 유효성 검증
 * - SecurityContext에 인증 정보 설정
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // 요청 헤더에서 JWT 토큰 추출
            String token = extractTokenFromRequest(request);
            
            // 토큰이 존재하는 경우에만 검증 수행
            if (StringUtils.hasText(token)) {
                // 토큰 유효성 검증
                if (!jwtTokenProvider.validateToken(token)) {
                    throw new InvalidTokenException("유효하지 않은 토큰입니다.");
                }
                
                String userId = jwtTokenProvider.getUserIdFromToken(token);
                List<String> roles = jwtTokenProvider.getRolesFromToken(token);
                
                // 권한 정보를 SimpleGrantedAuthority로 변환
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
                
                // 인증 토큰 생성 및 SecurityContext에 설정
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("JWT 토큰 인증 성공: userId={}, roles={}", userId, roles);
            }
        } catch (InvalidTokenException e) {
            log.error("JWT 토큰 인증 실패: {}", e.getMessage());
            // 토큰이 유효하지 않으면 예외를 던져서 GlobalExceptionHandler에서 처리
            throw e;
        } catch (Exception e) {
            log.error("JWT 토큰 인증 처리 중 오류 발생", e);
            // 기타 예외는 필터 체인을 계속 진행
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰을 추출
     * 
     * @param request HTTP 요청
     * @return JWT 토큰 (없으면 null)
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거
        }
        
        return null;
    }
} 