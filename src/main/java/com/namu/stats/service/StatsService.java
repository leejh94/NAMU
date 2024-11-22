package com.namu.stats.service;

import com.namu.common.dto.StatusDTO;
import com.namu.stats.mapper.StatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    @Autowired
    private StatsMapper statsMapper;

    public StatusDTO getTopStoresBySales(String region, String industry, int limit) {
        List<Map<String, Object>> storeList = statsMapper.findTopStoresBySales(region, industry, limit);

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
        List<Map<String, Object>> industryAvgSalesInfo = statsMapper.findIndustryAvgSaleCompanyInfos(region);


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
        List<Map<String, Object>> storeList = statsMapper.findLowestFranchiseFeeStores(region, industry, limit);
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
        List<Map<String, Object>> newOpeningsData = statsMapper.findIndustryNewOpeningsCount();
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