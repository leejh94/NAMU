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

    @GetMapping("/companyRegionTotalCount")
    public StatusDTO getCompanyRegionTotalCount(
            @RequestParam("companyNo") String companyNo
    ) {
        return searchService.getCompanyRegionTotalCount(companyNo);
    }
}
