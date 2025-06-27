package com.user.user_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger (OpenAPI) 설정
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .description("사용자 관리 서비스 API 문서")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("User Service Team")
                                .email("admin@example.com")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT 토큰을 입력하세요"))
                        .addSchemas("ApiResponse", new ObjectSchema()
                                .addProperty("success", new BooleanSchema().description("요청 성공 여부"))
                                .addProperty("message", new StringSchema().description("응답 메시지"))
                                .addProperty("data", new ObjectSchema().description("응답 데이터").nullable(true))
                                .description("표준 API 응답 래퍼"))
                        .addSchemas("JwtResponse", new ObjectSchema()
                                .addProperty("token", new StringSchema().description("JWT 액세스 토큰"))
                                .description("JWT 토큰 응답")));
    }
} 