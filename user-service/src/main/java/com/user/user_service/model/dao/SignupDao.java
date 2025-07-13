package com.user.user_service.model.dao;

import com.user.user_service.model.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 회원가입 관련 데이터베이스 작업을 처리하는 DAO 인터페이스
 */
@Mapper
public interface SignupDao {
    /**
     * 이메일로 사용자 존재 여부 확인
     * 
     * @param email 확인할 이메일
     * @return 존재 여부 (true: 존재함, false: 존재하지 않음)
     */
    boolean existsByEmail(String email);
    
    /**
     * 이메일로 사용자 조회
     * 
     * @param email 조회할 이메일
     * @return 사용자 정보 (없으면 null)
     */
    User findByEmail(String email);
    
    /**
     * 새로운 사용자 저장
     * 
     * @param user 저장할 사용자 정보
     */
    void save(User user);
} 