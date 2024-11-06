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
        Map<String, Object> salesInfo = searchMapper.findCompanySalesInfo(companyNo, region);
        StatusDTO response = new StatusDTO();

        if (salesInfo != null) {
            response.setCode(200);
            response.setMessage("매출 정보가 성공적으로 반환되었습니다.");
            response.setData(salesInfo);
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

        // 지역명 변환용 Map 정의
//        Map<String, String> regionMap = new HashMap<>();
//        regionMap.put("강원", "강원도");
//        regionMap.put("경기", "경기도");
//        regionMap.put("경남", "경상남도");
//        regionMap.put("경북", "경상북도");
//        regionMap.put("광주", "광주광역시");
//        regionMap.put("대구", "대구광역시");
//        regionMap.put("대전", "대전광역시");
//        regionMap.put("부산", "부산광역시");
//        regionMap.put("서울", "서울특별시");
//        regionMap.put("세종", "세종특별자치시");
//        regionMap.put("울산", "울산광역시");
//        regionMap.put("인천", "인천광역시");
//        regionMap.put("전남", "전라남도");
//        regionMap.put("전북", "전라북도");
//        regionMap.put("제주", "제주특별자치도");
//        regionMap.put("충남", "충청남도");
//        regionMap.put("충북", "충청북도");

        // totalCount 리스트 순회하면서 locale 값 변환
        if (totalCount != null) {
//            for (Map<String, Object> item : totalCount) {
//                String locale = (String) item.get("locale");
//                if (regionMap.containsKey(locale)) {
//                    item.put("locale", regionMap.get(locale));
//                }
//            }
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


}
