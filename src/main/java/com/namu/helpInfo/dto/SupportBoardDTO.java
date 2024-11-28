package com.namu.helpInfo.dto;

import lombok.Data;


@Data
public class SupportBoardDTO {
    private Long supportId;     // 고유 번호
    private String title;  // 제목
    private String link;  // 사이트 설명
    private String region;  // 지역
    private String createdAt; // 생성일
}
