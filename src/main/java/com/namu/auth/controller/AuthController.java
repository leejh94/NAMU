package com.namu.auth.controller;

import com.namu.auth.service.AuthService;
import com.namu.dto.StatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/oauth/login")
    public StatusDTO oauthLogin(@RequestBody Map<String, String> requestBody) {
        String provider = requestBody.get("provider");
        String code = requestBody.get("code");

        System.out.println("로그인 호출");
        if (provider == null || code == null) {
            return new StatusDTO(400, "provider 또는 code가 누락되었습니다.", null);
        }

        return authService.handleLogin(provider, code);
    }
}
