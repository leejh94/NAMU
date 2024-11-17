package com.namu.common.service;

import com.namu.dto.StatusDTO;
import com.namu.common.mapper.SearchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.jsoup.Jsoup;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class SearchService {

    @Autowired
    private SearchMapper searchMapper;

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;


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

    // 프랜차이즈 가맹비용
    public StatusDTO getFranchiseFee(String companyNo) {
        Map<String, Object> franchiseFeeData = searchMapper.findFranchiseFeeDetails(companyNo);
        StatusDTO response = new StatusDTO();

        if (franchiseFeeData != null) {
            response.setCode(200);
            response.setMessage("가맹 비용 정보가 성공적으로 반환되었습니다.");
            response.setData(franchiseFeeData);
        } else {
            response.setCode(404);
            response.setMessage("해당 회사의 가맹 비용 정보를 찾을 수 없습니다.");
            response.setData(null);
        }

        return response;
    }

    public StatusDTO getNaverNewsList(String companyNo, int count) {
        // 회사 기본 정보 가져오기
        Map<String, Object> companyBasicInfo = searchMapper.findCompanyBasicInfo(companyNo);
        String companyName = (String) companyBasicInfo.get("business_mark");

        // API URL 구성 - display는 항상 100으로 고정
        String apiUrl = String.format(
                "https://openapi.naver.com/v1/search/news.json?query=%s&display=100&start=1&sort=sim",
                companyName
        );

        RestTemplate restTemplate = new RestTemplate();

        // API 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        // 요청 엔티티 생성
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // API 호출
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Map.class);

            // 응답 데이터 필터링 및 count 제한
            List<Map<String, String>> items = ((List<Map<String, String>>) response.getBody().get("items")).stream()
                    .filter(item -> item.get("title").contains(companyName) || item.get("description").contains(companyName))
                    .map(item -> {
                        // pubDate 변환
                        String originalPubDate = item.get("pubDate");
                        String formattedPubDate = formatPubDate(originalPubDate);
                        item.put("pubDate", formattedPubDate);

                        // HTML 태그 및 특수문자 제거
                        item.put("title", cleanText(item.get("title")));
                        item.put("description", cleanText(item.get("description")));

                        // originallink 필드 제거
                        item.remove("originallink");
                        return item;
                    })
                    .sorted(Comparator.comparing(item -> item.get("pubDate").toString())) // pubDate 오름차순 정렬

                    .limit(count) // count 제한
                    .collect(Collectors.toList());

            // 성공 응답 구성
            StatusDTO status = new StatusDTO();
            status.setCode(200);
            status.setMessage("검색 결과가 성공적으로 반환되었습니다.");
            status.setData(items);
            return status;

        } catch (Exception e) {
            // 오류 응답 구성
            StatusDTO status = new StatusDTO();
            status.setCode(500);
            status.setMessage("네이버 뉴스 API 호출 실패: " + e.getMessage());
            status.setData(null);
            return status;
        }
    }

    // pubDate 변환 메서드
    private String formatPubDate(String pubDate) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

            LocalDateTime dateTime = LocalDateTime.parse(pubDate, inputFormatter);
            return dateTime.format(outputFormatter);
        } catch (Exception e) {
            System.err.println("날짜 변환 실패: " + pubDate);
            return pubDate;
        }
    }

    // HTML 태그 및 특수 문자 제거 메서드
    private String cleanText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Jsoup.parse(text).text();
    }
}