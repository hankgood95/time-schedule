package com.user.user_service.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

/**
 * JWT 토큰 생성, 검증, 파싱을 담당하는 유틸리티 클래스
 */
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret:defaultSecretKeyForDevelopmentOnly}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}") // 기본 24시간
    private long jwtExpiration;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    /**
     * JWT 토큰 생성
     * @param userId 사용자 ID
     * @param roles 사용자 권한 목록
     * @return 생성된 JWT 토큰
     */
    public String createToken(String userId, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        return Jwts.builder()
                .subject(userId)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();
    }
    
    /**
     * JWT 토큰 검증
     * @param token 검증할 JWT 토큰
     * @return 토큰 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * JWT 토큰에서 사용자 ID 추출
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * JWT 토큰에서 사용자 권한 추출
     * @param token JWT 토큰
     * @return 사용자 권한 목록
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return (List<String>) claims.get("roles");
        } catch (JwtException | IllegalArgumentException e) {
            return List.of();
        }
    }
    
    /**
     * JWT 토큰 만료 시간 확인
     * @param token JWT 토큰
     * @return 토큰 만료 여부
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }
} 