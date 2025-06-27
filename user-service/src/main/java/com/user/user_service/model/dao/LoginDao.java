package com.user.user_service.model.dao;

import com.user.user_service.model.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 로그인 관련 데이터베이스 작업을 처리하는 DAO 인터페이스
 */
@Mapper
public interface LoginDao {
    
    /**
     * 이메일로 사용자 조회
     * 
     * @param email 조회할 이메일
     * @return 사용자 정보 (없으면 null)
     */
    User findByEmail(String email);
} 