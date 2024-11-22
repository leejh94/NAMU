package com.namu.helpInfo.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 지원 사이트 정보 DTO
 */
@Data
public class SiteInfoDTO {
    private Long siteInfoId;     // 고유 번호
    private String siteName;         // 사이트명
    private String siteDescription;  // 사이트 설명
    private String siteLink;         // 사이트 링크
    private String imgLink;         // 이미지 링크
    private Integer indexOrder;       // 노출 순서
    private LocalDateTime createdAt; // 생성일
}
