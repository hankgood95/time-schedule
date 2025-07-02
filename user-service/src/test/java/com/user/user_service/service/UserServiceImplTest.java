package com.user.user_service.service;

import com.user.user_service.exception.UserNotFoundException;
import com.user.user_service.model.dao.UserDao;
import com.user.user_service.model.domain.User;
import com.user.user_service.model.domain.UserStatus;
import com.user.user_service.model.dto.response.UserInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * UserServiceImpl 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userId(1L)
                .email("test@example.com")
                .passwordHash("encodedPassword")
                .name("홍길동")
                .status(UserStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("사용자 ID로 사용자 정보 조회 성공")
    void getUserInfoById_Success() {
        // given
        String userId = "1";
        when(userDao.findById(1L)).thenReturn(testUser);

        // when
        UserInfoResponse result = userService.getUserInfoById(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getName()).isEqualTo("홍길동");
        assertThat(result.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 조회 시 예외 발생")
    void getUserInfoById_UserNotFound() {
        // given
        String userId = "999";
        when(userDao.findById(999L)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> userService.getUserInfoById(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("비활성화된 사용자 정보 조회 성공")
    void getUserInfoById_InactiveUser() {
        // given
        User inactiveUser = User.builder()
                .userId(2L)
                .email("inactive@example.com")
                .passwordHash("encodedPassword")
                .name("김철수")
                .status(UserStatus.INACTIVE)
                .build();

        String userId = "2";
        when(userDao.findById(2L)).thenReturn(inactiveUser);

        // when
        UserInfoResponse result = userService.getUserInfoById(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(2L);
        assertThat(result.getEmail()).isEqualTo("inactive@example.com");
        assertThat(result.getName()).isEqualTo("김철수");
        assertThat(result.getStatus()).isEqualTo("INACTIVE");
    }
} 