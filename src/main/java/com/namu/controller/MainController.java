package com.namu.controller;

import com.namu.dto.StatusDTO;
import com.namu.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private SearchService searchService;

    // 회사명 목록 가져오기 (브랜드명 or 회사명)
    @GetMapping("/companyNameList")
    public StatusDTO getCompanyNameList(
            @RequestParam("searchType") String searchType,
            @RequestParam("searchWord") String searchWord
    ) {
        return searchService.getCompanyNameList(searchType, searchWord);
    }

    @GetMapping("/companyBasicInfo")
    public StatusDTO getCompanyBasicInfo(
            @RequestParam("companyNo") String companyNo
    ) {
        return searchService.getCompanyBasicInfo(companyNo);
    }

    // 매장 및 업종 평균 매출 정보 가져오기
    @GetMapping("/companySalesInfo")
    public StatusDTO getCompanySalesInfo(
            @RequestParam("companyNo") String companyNo,
            @RequestParam("region") String region
    ) {
        return searchService.getCompanySalesInfo(companyNo, region);
    }

    // 특정 매장의 지역별 매장수
    @GetMapping("/companyRegionTotalCount")
    public StatusDTO getCompanyRegionTotalCount(
            @RequestParam("companyNo") String companyNo
    ) {
        return searchService.getCompanyRegionTotalCount(companyNo);
    }

    // 매출 상위 매장 목록
    @GetMapping("/topStoresBySales")
    public StatusDTO getTopStoresBySales(
            @RequestParam("region") String region,
            @RequestParam("industry") String industry,
            @RequestParam("limit") int limit
    ) {
        return searchService.getTopStoresBySales(region, industry, limit);
    }

    // 가맹 부담금이 가장 낮고 월평균 매출이 있는 매장 목록
    @GetMapping("/lowestFranchiseFeeStores")
    public StatusDTO getLowestFranchiseFeeStores(
            @RequestParam("region") String region,
            @RequestParam("industry") String industry,
            @RequestParam("limit") int limit
    ) {
        return searchService.getLowestFranchiseFeeStores(region, industry, limit);
    }
    
    // 지역별 매출 평균(return 15개 고정)
    @GetMapping("/industryAvgSalesInfo")
    public StatusDTO getIndustryAvgSalesInfo(
            @RequestParam("region") String region
    ) {
        return searchService.getIndustryAvgSalesInfo(region);
    }

    // 업종별 신규 개점 수 조회 (return 15개 고정)
    @GetMapping("/industryNewOpeningsCount")
    public StatusDTO getNewOpeningsCount() {
        return searchService.getNewOpeningsCount();
    }
}
