package com.namu.auth.service;

import com.namu.auth.mapper.AuthMapper;
import com.namu.util.NicknameGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NicknameService {

    private final AuthMapper authMapper;

    public NicknameService(AuthMapper authMapper) {
        this.authMapper = authMapper;
    }

    /**
     * 중복 없는 고유 닉네임 생성 (DB 조회 최소화)
     *
     * @return 고유한 닉네임
     */
    public String generateUniqueNickname() {
        String baseNickname = NicknameGenerator.generateNickname(); // 랜덤 닉네임 생성

        System.out.println("1");
        // DB에서 패턴에 맞는 닉네임 전체 조회 (e.g., "빛나는 호랑이%")
        List<String> existingNicknames = authMapper.findNicknamesByPattern(baseNickname + "%");
        System.out.println("2");

        // 중복되지 않는 고유 닉네임 찾기
        String uniqueNickname = baseNickname;
        int suffix = 1;

        while (existingNicknames.contains(uniqueNickname)) {
            uniqueNickname = baseNickname + suffix; // 중복되면 숫자를 붙임
            suffix++;
        }

        System.out.println(uniqueNickname);
        return uniqueNickname;
    }
}
