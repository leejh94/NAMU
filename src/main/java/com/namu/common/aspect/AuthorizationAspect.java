package com.namu.common.aspect;

import com.namu.common.annotation.RequiredRole;
import com.namu.common.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Aspect
@Component
public class AuthorizationAspect {

    private final HttpServletRequest request;

    public AuthorizationAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Before("@annotation(requiredRole)") // @RequiredRole 애노테이션이 있는 메서드 실행 전
    public void checkAuthorization(RequiredRole requiredRole) {
        String token = request.getHeader("Authorization"); // 헤더에서 토큰 추출
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 필요합니다.");
        }

        try {
            // JWT 토큰에서 사용자 역할 확인
            token = token.substring(7); // "Bearer " 부분 제거
            Claims claims = JwtTokenUtil.validateToken(token);
            String userRole = claims.get("role", String.class);

            // 역할이 허용된 목록에 포함되는지 확인
            if (Arrays.stream(requiredRole.value()).noneMatch(userRole::equals)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "토큰 검증 실패: " + e.getMessage());
        }
    }
}
