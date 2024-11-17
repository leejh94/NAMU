package com.namu.auth.controller;

import com.namu.dto.StatusDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/logout")
    public StatusDTO logout() {
        // 로그아웃 처리
        return new StatusDTO();
    }

    @GetMapping("/oauth/kakao")
    public StatusDTO kakaoLogin(@RequestParam String code) {
        // 네이버 로그인 처리
        return new StatusDTO();
    }

}
