package com.namu.common.util;


import java.util.Random;

public class NicknameGenerator {

    // 랜덤 단어 리스트
    private static final String[] PREFIXES = {"빛나는", "빠른", "강한", "귀여운", "신비한" , "재빠른" , "날렵한"};
    private static final String[] SUFFIXES = {"호랑이", "늑대", "별", "달", "고양이", "독수리"};

    // 랜덤 닉네임 생성 메서드
    public static String generateNickname() {
        Random random = new Random();
        String prefix = PREFIXES[random.nextInt(PREFIXES.length)];
        String suffix = SUFFIXES[random.nextInt(SUFFIXES.length)];
        return prefix + suffix; // 형식: "형용사 + 명사"
    }


}