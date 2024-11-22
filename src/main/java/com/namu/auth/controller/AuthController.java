package com.namu.auth.controller;

import com.namu.auth.service.AuthService;
import com.namu.common.dto.StatusDTO;
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


    @PostMapping("/admin/login")
    public StatusDTO adminLogin(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        System.out.println(username);
        System.out.println(password);
        System.out.println("관리자 로그인");


        if (username == null || password == null) {
            return new StatusDTO(400, "username 또는 password가 누락되었습니다.", null);
        }


        return authService.adminLogin(username, password);
    }

}
