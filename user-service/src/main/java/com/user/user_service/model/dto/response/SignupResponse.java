package com.user.user_service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원가입 성공 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원가입 성공 응답")
public class SignupResponse {
    @Schema(description = "JWT 액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;
    
    @Schema(description = "홈화면 이동 안내 메시지", example = "회원가입이 완료되었습니다. 홈화면으로 이동합니다.")
    private String redirectMessage;

    @Schema(description = "회원가입된 사용자 ID", example = "1")
    private Long userId;
}
