package com.namu.auth.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namu.common.dto.StatusDTO;
import com.namu.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * KakaoLoginProvider는 Kakao 로그인 로직을 처리하는 클래스입니다.
 * LoginProvider 인터페이스를 구현하여 Kakao의 OAuth2 인증을 수행합니다.
 */
@Component("kakao")
@RequiredArgsConstructor
public class KakaoLoginProvider implements LoginProvider {

    private final WebClient webClient = WebClient.create(); // WebClient를 사용하여 HTTP 요청 수행

    // 카카오 API 설정 값 (application.yml에서 주입받음)
    @Value("${kakao.api.client-id}")
    private String clientId;

    @Value("${kakao.api.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.api.token-uri}")
    private String tokenUri;

    @Value("${kakao.api.user-info-uri}")
    private String userInfoUri;

    /**
     * 카카오 인증 코드를 사용하여 로그인 처리.
     *
     * @param code 카카오 인증 서버에서 제공받은 Authorization Code
     * @return StatusDTO (로그인 성공 또는 실패 결과를 포함)
     */
    @Override
    public StatusDTO login(String code) {
        try {
            // 1. Access Token 요청
            String accessToken = getAccessToken(code);

            // 2. 사용자 정보 요청
            User user = getUserInfo(accessToken);

            // 3. 성공 시 사용자 정보를 포함한 상태 반환
            return new StatusDTO(200, "로그인 성공", user);
        } catch (Exception e) {
            // 4. 실패 시 에러 메시지를 포함한 상태 반환
            return new StatusDTO(500, "카카오 로그인 실패: " + e.getMessage(), null);
        }
    }

    /**
     * 카카오 API를 호출하여 Access Token을 획득합니다.
     *
     * @param code 카카오 인증 서버에서 제공받은 Authorization Code
     * @return Access Token (String)
     * @throws Exception Access Token 요청 실패 시 발생
     */
    private String getAccessToken(String code) throws Exception {
        // HTTP POST 요청으로 Access Token 획득
        String response = webClient.post()
                .uri(tokenUri)
                .bodyValue("grant_type=authorization_code&client_id=" + clientId +
                        "&redirect_uri=" + redirectUri +
                        "&code=" + code)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // JSON 응답 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response);
        if (jsonNode.has("error")) {
            // 오류 발생 시 예외 처리
            throw new Exception("카카오 Access Token 요청 실패: " + jsonNode.get("error_description").asText());
        }

        // Access Token 반환
        return jsonNode.get("access_token").asText();
    }

    /**
     * Access Token을 사용하여 카카오 사용자 정보를 가져옵니다.
     *
     * @param accessToken 카카오에서 발급한 Access Token
     * @return User 객체 (사용자 정보 포함)
     * @throws Exception 사용자 정보 요청 실패 시 발생
     */
    private User getUserInfo(String accessToken) throws Exception {
        // HTTP GET 요청으로 사용자 정보 획득
        String response = webClient.get()
                .uri(userInfoUri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // JSON 응답 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response);
        if (!jsonNode.has("id")) {
            // 사용자 정보가 없는 경우 예외 처리
            throw new Exception("카카오 사용자 정보 요청 실패");
        }

        // 사용자 정보를 User 객체로 변환
        User user = new User();
        user.setProvider("kakao"); // 제공자 이름 설정
        user.setProviderId(jsonNode.get("id").asText()); // 카카오 사용자 고유 ID
        user.setUsername(jsonNode.get("properties").get("nickname").asText()); // 카카오에서 제공한 닉네임 설정


        return user; // 사용자 정보 반환
    }
}
