package com.namu.entity;

import lombok.Data;

@Data
public class User {
    private Long userId;
    private String username;
    private String nickname;
    private String provider;
    private String providerId;
    private String role;
}
