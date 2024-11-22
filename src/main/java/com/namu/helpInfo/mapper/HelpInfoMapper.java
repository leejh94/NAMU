package com.namu.helpInfo.mapper;

import com.namu.helpInfo.dto.SiteInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

}
