package com.user.user_service.db;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
class DBConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("MySQL 연결 테스트")
    void testDatabaseConnection() {
        // 설정 정보 출력
        log.info("Active profiles: {}", String.join(", ", env.getActiveProfiles()));
        log.info("Database URL: {}", env.getProperty("spring.datasource.url"));
        log.info("Database Username: {}", env.getProperty("spring.datasource.username"));
        log.info("Database Driver: {}", env.getProperty("spring.datasource.driver-class-name"));

        try {
            // 직접 Connection 시도
            Connection conn = dataSource.getConnection();
            log.info("Connection successful! URL: {}", conn.getMetaData().getURL());
            conn.close();
        } catch (Exception e) {
            log.error("Connection failed!", e);
        }

        // when
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

        // then
        assertThat(result).isEqualTo(1);
    }
} 