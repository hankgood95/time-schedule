package com.user.user_service.model.dao;

import com.user.user_service.model.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 사용자 정보 조회를 위한 DAO 인터페이스
 */
@Mapper
public interface UserDao {
    
    /**
     * 이메일을 통해 사용자 정보를 조회
     * 
     * @param email 사용자 이메일
     * @return 사용자 정보 (없으면 null)
     */
    User findByEmail(@Param("email") String email);
    
    /**
     * 사용자 ID를 통해 사용자 정보를 조회
     * 
     * @param userId 사용자 ID
     * @return 사용자 정보 (없으면 null)
     */
    User findById(@Param("userId") Long userId);
} 