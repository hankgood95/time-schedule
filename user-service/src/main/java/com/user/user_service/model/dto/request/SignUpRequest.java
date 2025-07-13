package com.user.user_service.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원가입 요청을 위한 DTO 클래스
 * 
 * 이 클래스는 사용자로부터 회원가입에 필요한 정보를 받아오는 데 사용됩니다.
 * 각 필드에는 유효성 검사를 위한 제약조건이 포함되어 있습니다.
 * 
 * 주요 어노테이션 설명:
 * 1. Lombok 어노테이션
 *    - @Getter: 모든 필드에 대한 getter 메서드 자동 생성
 *    - @Setter: 모든 필드에 대한 setter 메서드 자동 생성
 *    - @NoArgsConstructor: 파라미터가 없는 기본 생성자 자동 생성
 * 
 * 2. Validation 어노테이션
 *    - @NotBlank: null, 빈 문자열(""), 공백 문자열(" ")을 허용하지 않음
 *    - @Email: 이메일 형식 검증 (예: user@example.com)
 *    - @Size: 문자열의 길이 제한 (min, max 속성으로 지정)
 *    - @Pattern: 정규표현식을 사용한 문자열 패턴 검증
 */
//lombok 어노테이션
@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {

    /**
     * 사용자 이메일
     * - @NotBlank: null, 빈 문자열, 공백만 있는 문자열을 허용하지 않음
     * - @Email: 이메일 형식 검증
     */
    // validation 어노테이션
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    /**
     * 사용자 비밀번호
     * - @NotBlank: null, 빈 문자열, 공백만 있는 문자열을 허용하지 않음
     * - @Size: 최소 8자, 최대 20자
     * - @Pattern: 영문, 숫자, 특수문자를 포함해야 함
     */
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
        message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    /**
     * 사용자 이름
     * - @NotBlank: null, 빈 문자열, 공백만 있는 문자열을 허용하지 않음
     * - @Size: 최소 2자, 최대 20자
     */
    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하여야 합니다.")
    private String name;
} 