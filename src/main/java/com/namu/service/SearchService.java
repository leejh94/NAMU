package com.namu.service;

import com.namu.dto.StatusDTO;
import com.namu.mapper.SearchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private SearchMapper searchMapper;

    public StatusDTO getCompanyNameList(String searchType, String searchWord) {
        List<Map<String, Object>> companyList = searchMapper.findCompanyNames(searchType, searchWord);

        StatusDTO response = new StatusDTO();
        if (companyList != null && !companyList.isEmpty()) {
            response.setCode(200);
            response.setMessage("검색 결과가 성공적으로 반환되었습니다.");
            response.setData(companyList);
        } else {
            response.setCode(404);
            response.setMessage("검색 결과가 없습니다.");
            response.setData(null);
        }
        return response;
    }

    public StatusDTO getCompanyBasicInfo(String companyNo) {
        Map<String, Object> companyBasicInfo = searchMapper.findCompanyBasicInfo(companyNo);
        StatusDTO response = new StatusDTO();

        if (companyBasicInfo != null) {
            response.setCode(200);
            response.setMessage("검색 결과가 성공적으로 반환되었습니다.");
            response.setData(companyBasicInfo);
        } else {
            response.setCode(404);
            response.setMessage("검색 결과가 없습니다.");
            response.setData(null);
        }
        return response;
    }

    // 회사 매출 및 업종 평균 매출 정보
    public StatusDTO getCompanySalesInfo(String companyNo, String region) {
        // 개별 매장 정보 가져오기
        Map<String, Object> companySalesInfo = searchMapper.findCompanySalesInfo(companyNo, region);

        StatusDTO response = new StatusDTO();

        if (companySalesInfo != null) {
            // 업종 평균 매출 정보 가져오기
            String industry = (String) companySalesInfo.get("industry");
            Map<String, Object> industryAvgSalesInfo = searchMapper.findIndustryAvgSalesInfo(region, industry);

            // 업종 평균 정보가 있으면 추가
            if (industryAvgSalesInfo != null) {
                companySalesInfo.putAll(industryAvgSalesInfo);
            }

            response.setCode(200);
            response.setMessage("매출 정보가 성공적으로 반환되었습니다.");
            response.setData(companySalesInfo);
        } else {
            response.setCode(404);
            response.setMessage("매출 정보가 없습니다.");
            response.setData(null);
        }
        return response;
    }

    // 회사 매출 및 업종 평균 매출 정보
    public StatusDTO getCompanyRegionTotalCount(String companyNo) {
        List<Map<String, Object>> totalCount = searchMapper.findCompanyRegionTotalCount(companyNo);
        StatusDTO response = new StatusDTO();

        if (totalCount != null) {

            response.setCode(200);
            response.setMessage("가맹 현황 정보가 성공적으로 반환되었습니다.");
            response.setData(totalCount);
        } else {
            response.setCode(404);
            response.setMessage("정보가 없습니다.");
            response.setData(null);
        }

        return response;
    }

    public StatusDTO getTopStoresBySales(String region, String industry, int limit) {
        List<Map<String, Object>> storeList = searchMapper.findTopStoresBySales(region, industry, limit);

        StatusDTO response = new StatusDTO();
        if (storeList != null && !storeList.isEmpty()) {
            response.setCode(200);
            response.setMessage("매출 정보가 성공적으로 반환되었습니다.");
            response.setData(storeList);
        } else {
            response.setCode(404);
            response.setMessage("매출 정보가 없습니다.");
            response.setData(null);
        }
        return response;
    }

    public StatusDTO getIndustryAvgSalesInfo(String region) {
        List<Map<String, Object>> industryAvgSalesInfo = searchMapper.findIndustryAvgSaleCompanyInfos(region);


        StatusDTO response = new StatusDTO();
        if (industryAvgSalesInfo != null && !industryAvgSalesInfo.isEmpty()) {
            response.setCode(200);
            response.setMessage("매출 정보가 성공적으로 반환되었습니다.");
            response.setData(industryAvgSalesInfo);
        } else {
            response.setCode(404);
            response.setMessage("매출 정보가 없습니다.");
            response.setData(null);
        }
        return response;
    }

    public StatusDTO getLowestFranchiseFeeStores(String region, String industry, int limit) {
        List<Map<String, Object>> storeList = searchMapper.findLowestFranchiseFeeStores(region, industry, limit);
        StatusDTO response = new StatusDTO();

        if (storeList != null && !storeList.isEmpty()) {
            response.setCode(200);
            response.setMessage("매장 정보가 성공적으로 반환되었습니다.");
            response.setData(storeList);
        } else {
            response.setCode(404);
            response.setMessage("정보가 없습니다.");
            response.setData(null);
        }

        return response;
    }

    // 신규 개점 수 조회
    public StatusDTO getNewOpeningsCount() {
        List<Map<String, Object>> newOpeningsData = searchMapper.findIndustryNewOpeningsCount();
        StatusDTO response = new StatusDTO();

        if (newOpeningsData != null) {
            response.setCode(200);
            response.setMessage("신규 개점 수가 성공적으로 반환되었습니다.");
            response.setData(newOpeningsData);
        } else {
            response.setCode(404);
            response.setMessage("신규 개점 수 정보가 없습니다.");
            response.setData(null);
        }

        return response;
    }

}
