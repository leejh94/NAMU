package com.namu.auth.mapper;

import com.namu.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AuthMapper는 MyBatis를 사용하여 사용자 및 권한 관련 데이터베이스 작업을 수행합니다.
 */
@Mapper
public interface AuthMapper {

    /**
     * 제공자와 제공자 ID를 기준으로 사용자를 조회합니다.
     *
     * @param provider 로그인 제공자 (예: kakao, google 등)
     * @param providerId 로그인 제공자에서 발급한 고유 사용자 ID
     * @return User 객체 (사용자가 존재하지 않을 경우 null 반환)
     */
    User findUserByProvider(@Param("provider") String provider, @Param("providerId") String providerId);

    /**
     * 닉네임 패턴에 해당하는 닉네임 목록 조회
     *
     * @param pattern 닉네임 패턴 (e.g., "빛나는 호랑이%")
     * @return 닉네임 목록
     */
    List<String> findNicknamesByPattern(@Param("pattern") String pattern);

    /**
     * 새 사용자를 데이터베이스에 등록합니다.
     *
     * @param user 새로 등록할 사용자 객체 (username, provider, providerId 포함)
     */
    void registerUser(User user);

    /**
     * 특정 사용자에게 기본 권한(USER)을 부여합니다.
     *
     * @param userId 권한을 부여할 사용자 ID (users 테이블의 user_id 참조)
     */
    void assignDefaultRole(@Param("userId") Long userId);
}
