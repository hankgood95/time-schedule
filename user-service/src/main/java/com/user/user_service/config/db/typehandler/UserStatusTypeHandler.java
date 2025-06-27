package com.user.user_service.config.db.typehandler;

import com.user.user_service.model.domain.UserStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserStatus enum을 위한 MyBatis TypeHandler
 * 
 * 이 클래스는 UserStatus enum과 데이터베이스의 문자열 값 간의 변환을 처리합니다.
 * MyBatis가 자동으로 이 TypeHandler를 사용하여 변환을 처리합니다.
 * 
 * 변환 규칙:
 * - Java → DB: UserStatus.ACTIVE → "active"
 * - DB → Java: "active" → UserStatus.ACTIVE
 * 
 * 자동 호출 과정:
 * 1. MyBatis가 SQL 쿼리를 실행할 때
 * 2. 결과를 Java 객체로 변환하는 과정에서
 * 3. UserStatus 타입의 필드를 만나면
 * 4. 자동으로 이 TypeHandler의 적절한 메서드를 호출
 * 
 * 실제 사용 예시:
 * 1. 컬럼 이름으로 읽기:
 *    String sql = "SELECT status FROM users WHERE id = ?";
 *    ResultSet rs = preparedStatement.executeQuery();
 *    UserStatus status = typeHandler.getNullableResult(rs, "status");
 * 
 * 2. 컬럼 인덱스로 읽기:
 *    String sql = "SELECT id, name, status FROM users";
 *    ResultSet rs = preparedStatement.executeQuery();
 *    UserStatus status = typeHandler.getNullableResult(rs, 2);  // status는 3번째 컬럼
 * 
 * 3. 저장 프로시저 결과 읽기:
 *    String sql = "{CALL get_user_status(?)}";
 *    CallableStatement cs = connection.prepareCall(sql);
 *    cs.setInt(1, userId);
 *    cs.execute();
 *    UserStatus status = typeHandler.getNullableResult(cs, 1);  // 첫 번째 출력 파라미터
 */
@MappedTypes(UserStatus.class)  // 이 TypeHandler가 UserStatus enum을 처리한다고 MyBatis에 알림
public class UserStatusTypeHandler extends BaseTypeHandler<UserStatus> {

    /**
     * Java 객체를 데이터베이스에 저장할 때 호출되는 메서드
     * 
     * @param ps PreparedStatement 객체
     * @param i 파라미터 위치
     * @param parameter 변환할 UserStatus enum 값
     * @param jdbcType JDBC 타입
     * @throws SQLException SQL 예외 발생 시
     * 
     * 예시: UserStatus.ACTIVE → "active"로 변환
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UserStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name().toLowerCase());
    }

    /**
     * 데이터베이스에서 컬럼 이름으로 값을 읽어올 때 호출되는 메서드
     * 
     * @param rs ResultSet 객체
     * @param columnName 컬럼 이름
     * @return 변환된 UserStatus enum 값
     * @throws SQLException SQL 예외 발생 시
     * 
     * 예시: DB의 "active" → UserStatus.ACTIVE로 변환
     * 
     * 사용 예시:
     * String sql = "SELECT status FROM users WHERE id = ?";
     * ResultSet rs = preparedStatement.executeQuery();
     * UserStatus status = typeHandler.getNullableResult(rs, "status");
     */
    @Override
    public UserStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertToEnum(rs.getString(columnName));
    }

    /**
     * 데이터베이스에서 컬럼 인덱스로 값을 읽어올 때 호출되는 메서드
     * 
     * @param rs ResultSet 객체
     * @param columnIndex 컬럼 인덱스
     * @return 변환된 UserStatus enum 값
     * @throws SQLException SQL 예외 발생 시
     * 
     * 예시: DB의 "active" → UserStatus.ACTIVE로 변환
     * 
     * 사용 예시:
     * String sql = "SELECT id, name, status FROM users";
     * ResultSet rs = preparedStatement.executeQuery();
     * UserStatus status = typeHandler.getNullableResult(rs, 2);  // status는 3번째 컬럼
     */
    @Override
    public UserStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertToEnum(rs.getString(columnIndex));
    }

    /**
     * 저장 프로시저의 결과를 읽어올 때 호출되는 메서드
     * 
     * @param cs CallableStatement 객체
     * @param columnIndex 컬럼 인덱스
     * @return 변환된 UserStatus enum 값
     * @throws SQLException SQL 예외 발생 시
     * 
     * 예시: DB의 "active" → UserStatus.ACTIVE로 변환
     * 
     * 사용 예시:
     * String sql = "{CALL get_user_status(?)}";
     * CallableStatement cs = connection.prepareCall(sql);
     * cs.setInt(1, userId);
     * cs.execute();
     * UserStatus status = typeHandler.getNullableResult(cs, 1);  // 첫 번째 출력 파라미터
     */
    @Override
    public UserStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertToEnum(cs.getString(columnIndex));
    }

    /**
     * 문자열을 UserStatus enum으로 변환하는 공통 메서드
     * 
     * @param value 변환할 문자열 값
     * @return 변환된 UserStatus enum 값
     * 
     * 예시: "active" → UserStatus.ACTIVE로 변환
     */
    private UserStatus convertToEnum(String value) {
        return value == null ? null : UserStatus.valueOf(value.toUpperCase());
    }
} 