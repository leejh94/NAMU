package com.namu.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret.key}")
    private String secretKeyValue; // @Value는 인스턴스 필드에만 적용

    private static String secretKey; // static 필드로 사용하기 위해 별도 설정

    private static final long EXPIRATION_TIME = 3600000; // 1시간 (밀리초 단위)

    /**
     * static 필드에 값을 설정하기 위해 PostConstruct 사용
     */
    @PostConstruct
    public void init() {
        JwtTokenUtil.secretKey = this.secretKeyValue; // static 필드에 값을 할당
    }

    /**
     * JWT 토큰 생성 메서드
     * @param userId 사용자 고유 ID
     * @param role 사용자 권한 (예: USER, ADMIN)
     * @return 생성된 JWT 토큰
     */
    public static String generateToken(Long userId, String role) {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("JWT 시크릿 키가 설정되지 않았습니다.");
        }

        return Jwts.builder()
                .setSubject(userId.toString()) // 사용자 ID를 subject로 설정
                .claim("role", role)          // 사용자 권한 추가
                .setIssuedAt(new Date())      // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // HMAC-SHA256 서명
                .compact();
    }

    /**
     * JWT 토큰 해석 메서드
     * @param token JWT 토큰
     * @return Claims 객체 (토큰 내부 정보)
     * @throws Exception 토큰이 유효하지 않은 경우
     */
    public static Claims validateToken(String token) throws Exception {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("JWT 시크릿 키가 설정되지 않았습니다.");
        }

        return Jwts.parser()
                .setSigningKey(secretKey) // 비밀 키로 서명 검증
                .parseClaimsJws(token)
                .getBody();
    }
}
