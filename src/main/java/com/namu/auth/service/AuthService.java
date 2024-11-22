package com.namu.auth.service;

import com.namu.auth.mapper.AuthMapper;
import com.namu.auth.provider.LoginProvider;
import com.namu.common.dto.StatusDTO;
import com.namu.auth.entity.User;
import com.namu.common.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * AuthService는 사용자 인증 및 회원가입 로직을 담당합니다.
 * 로그인 제공자별 로직은 LoginProvider를 통해 위임받아 처리하며,
 * 로그인 전 사용자 정보 확인과 필요 시 회원가입을 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    // Spring에서 LoginProvider 구현체들을 Map으로 주입받아 관리 (key: provider 이름)
    private final Map<String, LoginProvider> loginProviders;

    // 사용자와 권한 관련 데이터베이스 작업을 수행하는 MyBatis 매퍼
    private final AuthMapper authMapper;

    private final NicknameService nicknameService;

    /**
     * 로그인 및 회원가입 처리 메서드
     * @param provider 로그인 제공자 (예: kakao, google 등)
     * @param code 인증 코드 (OAuth 2.0 인증 단계에서 제공자에게 받은 코드)
     * @return StatusDTO 로그인 결과를 반환 (성공, 실패 메시지 및 데이터 포함)
     */
    public StatusDTO handleLogin(String provider, String code) {
        // 1. 로그인 제공자 확인
        // 주어진 provider 이름을 통해 LoginProvider 구현체를 찾음
        LoginProvider loginProvider = loginProviders.get(provider.toLowerCase());
        if (loginProvider == null) {
            // provider가 지원되지 않는 경우 400 상태 반환
            return new StatusDTO(400, "지원하지 않는 로그인 제공자입니다.", null);
        }

        // 2. 제공자 로그인 처리
        // LoginProvider 구현체의 login 메서드를 호출하여 제공자별 로그인 로직 수행
        StatusDTO providerStatus = loginProvider.login(code);
        if (providerStatus.getCode() != 200) {
            // 로그인 실패 시 반환된 StatusDTO를 그대로 리턴
            return providerStatus;
        }

        // 3. 로그인 성공 시 반환된 사용자 정보 추출
        User user = (User) providerStatus.getData();

        // 4. 기존 사용자 확인
        // 주어진 provider와 provider_id를 사용하여 데이터베이스에서 사용자를 검색
        User existingUser = authMapper.findUserByProvider(provider, user.getProviderId());

        if (existingUser == null) {

            user.setNickname(nicknameService.generateUniqueNickname()); // 고유 닉네임 생성
            // 4-1. 기존 사용자가 없으면 신규 회원가입 처리
            authMapper.registerUser(user); // users 테이블에 사용자 정보 삽입
            authMapper.assignDefaultRole(user.getUserId()); // user_role 테이블에 기본 권한 (USER) 부여
            user.setRole("USER");
        } else {
            // 4-2. 기존 사용자가 있으면 그 사용자 정보를 사용
            user = existingUser;
        }

        // **JWT 토큰 생성**
        String jwtToken = JwtTokenUtil.generateToken(user.getUserId(), "USER");

        // 5. 최종 로그인 성공 응답 생성
        // 사용자 정보와 함께 성공 메시지 반환
        return new StatusDTO(200, "로그인 성공", Map.of(
                "nickname", user.getNickname(),
                "role", user.getRole(),
                "jwtToken", jwtToken
        ));
    }


    public StatusDTO adminLogin(String username, String password) {
        // 1. 관리자 계정 조회
        User admin = authMapper.findAdminByUsernameAndPassword(username, password);

        if (admin == null) {
            // 아이디 또는 비밀번호가 잘못된 경우
            return new StatusDTO(401, "아이디 또는 비밀번호가 잘못되었습니다.", null);
        }

        // 2. 권한 확인 (관리자 권한인지 확인)
        if (!"ADMIN".equals(admin.getRole()) && !"MASTER".equals(admin.getRole())) {
            return new StatusDTO(403, "관리자 권한이 없습니다.", null);
        }

        // 3. JWT 토큰 생성
        String jwtToken = JwtTokenUtil.generateToken(admin.getUserId(), "ADMIN");

        // 4. 성공 응답 생성
        return new StatusDTO(200, "관리자 로그인 성공", Map.of(
                "nickname", admin.getUsername(), // username을 nickname에 매핑
                "role", admin.getRole(),
                "jwtToken", jwtToken
        ));
    }
}
