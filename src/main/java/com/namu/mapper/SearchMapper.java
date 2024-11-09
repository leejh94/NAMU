package com.namu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SearchMapper {

    // 회사명 목록
    List<Map<String, Object>> findCompanyNames(
            @Param("searchType") String searchType,
            @Param("searchWord") String searchWord
    );

    // 특정 매장 기본 정보
    Map<String, Object> findCompanyBasicInfo(
            @Param("companyNo") String companyNo
    );

    // 특정 매장 매출 정보
    Map<String, Object> findCompanySalesInfo(
            @Param("companyNo") String companyNo,
            @Param("region") String region
    );

    // 특정 지역,업종의 평균 매출 정보
    Map<String, Object> findIndustryAvgSalesInfo(
            @Param("region") String region,
            @Param("industry") String industry
    );

    // 특정 매장 지역 매장수
    List<Map<String, Object>> findCompanyRegionTotalCount(
            @Param("companyNo") String companyNo
    );


}

