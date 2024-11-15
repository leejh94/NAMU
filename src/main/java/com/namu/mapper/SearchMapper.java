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

    // 특정 지역 업종의 매출 상위
    List<Map<String, Object>> findTopStoresBySales(
            @Param("region") String region,
            @Param("industry") String industry,
            @Param("limit") int limit
    );

    // 특정 지역,업종의 평균 매출 정보
    List<Map<String, Object>> findIndustryAvgSaleCompanyInfos(
            @Param("region") String region
    );

    // 가맹 부담금이 가장 낮고 월평균 매출이 있는 매장 조회
    List<Map<String, Object>> findLowestFranchiseFeeStores(
            @Param("region") String region,
            @Param("industry") String industry,
            @Param("limit") int limit
    );

    // 업종별 신규 개점 수 조회 메소드
    List<Map<String, Object>> findIndustryNewOpeningsCount();

    // 가맹비용 조회
    Map<String, Object> findFranchiseFeeDetails(@Param("companyNo") String companyNo);

}

