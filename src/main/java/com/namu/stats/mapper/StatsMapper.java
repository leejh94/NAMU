package com.namu.stats.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatsMapper {

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

}

