package com.namu.helpInfo.mapper;

import com.namu.helpInfo.dto.SiteInfoDTO;
import com.namu.helpInfo.dto.SupportBoardDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface HelpInfoMapper {
    /**
     * 지원 사이트 정보 목록 조회
     * @return SiteInfoDTO 리스트
     */
    List<SiteInfoDTO> getInfoSiteList();


    void insertSiteInfo(
            @Param("title") String title,
            @Param("description") String description,
            @Param("siteLink") String siteLink,
            @Param("imgLink") String imgLink
    );

    /**
     * 사이트 순서 업데이트
     * @param siteInfoId 사이트 ID
     * @param indexOrder 업데이트할 순서
     */
    void updateSiteOrder(
            @Param("siteInfoId") int siteInfoId,
            @Param("indexOrder") int indexOrder
    );

    /**
     * 사이트 정보 삭제
     * @param siteInfoId 삭제할 사이트의 ID
     */
    void deleteSiteInfo(@Param("siteInfoId") int siteInfoId);

    /**
     * 사이트 정보 수정
     * @param siteInfoId 사이트 ID
     * @param title 제목
     * @param description 설명
     * @param link 링크
     * @param imgLink 이미지 링크 (null일 경우 기존 이미지 유지)
     */
    void updateSiteInfo(
            @Param("siteInfoId") int siteInfoId,
            @Param("title") String title,
            @Param("description") String description,
            @Param("link") String link,
            @Param("imgLink") String imgLink
    );

    // 지원 게시글 목록 조회
    List<SupportBoardDTO> getSupportBoardList(@Param("region") String region);

    // 지원 게시글 추가
    void insertSupportBoard(
            @Param("title") String title,
            @Param("link") String link,
            @Param("region") String region
    );

    // 지원 게시글 수정
    void updateSupportBoard(
            @Param("supportId") int supportId,
            @Param("title") String title,
            @Param("link") String link,
            @Param("region") String region
    );

    // 지원 게시글 삭제
    void deleteSupportBoard(@Param("supportId") int supportId);


}
