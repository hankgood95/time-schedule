package com.user.user_service.service;

import com.user.user_service.exception.UserNotFoundException;
import com.user.user_service.model.dao.UserDao;
import com.user.user_service.model.domain.User;
import com.user.user_service.model.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserService 인터페이스의 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public UserInfoResponse getUserInfoById(String userId) {
        // 사용자 ID로 사용자 조회
        User user = userDao.findById(Long.parseLong(userId));
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // UserInfoResponse로 변환하여 반환
        return new UserInfoResponse(
            user.getUserId(),
            user.getEmail(),
            user.getName(),
            user.getStatus().name()
        );
    }
} 