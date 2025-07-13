package com.user.user_service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT 토큰 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "JWT 토큰 응답")
public class JwtResponse {
    @Schema(description = "JWT 액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
} 