package com.namu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SearchMapper {
    List<Map<String, Object>> findCompanyNames(
            @Param("searchType") String searchType,
            @Param("searchWord") String searchWord
    );

    Map<String, Object> findCompanyBasicInfo(
            @Param("companyNo") String companyNo
    );

    // 매장 및 업종 평균 매출 정보 조회
    Map<String, Object> findCompanySalesInfo(
            @Param("companyNo") String companyNo,
            @Param("region") String region
    );

    List<Map<String, Object>> findCompanyRegionTotalCount(
            @Param("companyNo") String companyNo
    );


}

