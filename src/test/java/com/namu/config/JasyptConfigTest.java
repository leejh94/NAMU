package com.namu.config;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JasyptConfigTest {

    private PooledPBEStringEncryptor encryptor;

    private String plainText;
    private String encryptKey = "lee9494"; // 시크릿 키

    @BeforeEach
    public void setUp() {
        plainText = "dlwogns9494!"; // 원본 문자

        // JasyptConfig와 동일한 설정을 사용하여 encryptor 초기화
        encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(encryptKey);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
    }

    @Test
    public void testEncryptAndDecrypt() {
        // 암호화
        String encryptedText = encryptor.encrypt(plainText);
        System.out.println("Encrypted Text: " + encryptedText);

        // 복호화
        String decryptedText = encryptor.decrypt(encryptedText);
        System.out.println("Decrypted Text: " + decryptedText);

        // 원본과 복호화된 값이 같은지 확인
        assertEquals(plainText, decryptedText);
    }
}
