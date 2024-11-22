package com.namu.stats.controller;

import com.namu.common.dto.StatusDTO;
import com.namu.stats.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService searchService;

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
