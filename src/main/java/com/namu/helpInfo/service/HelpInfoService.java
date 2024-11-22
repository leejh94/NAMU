package com.namu.helpInfo.service;

import com.namu.common.dto.StatusDTO;
import com.namu.helpInfo.dto.SiteInfoDTO;
import com.namu.helpInfo.mapper.HelpInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class HelpInfoService {

    private static final String IMGBB_API_URL = "https://api.imgbb.com/1/upload";
    private static final String IMGBB_API_KEY = "06948c043a9ccfd46abb2edf5ce0c025"; // IMGBB API 키

    @Autowired
    private HelpInfoMapper helpInfoMapper;

    public StatusDTO getInfoSiteList() {
        try {
            List<SiteInfoDTO> siteInfoList = helpInfoMapper.getInfoSiteList();
            return new StatusDTO(200, "목록 조회 성공", siteInfoList);
        } catch (Exception e) {
            return new StatusDTO(500, "목록 조회 실패: " + e.getMessage(), null);
        }
    }

    public StatusDTO addSiteInfo(String img, String sitelink,String title, String description) {
        try {
            // Base64 데이터 확인
            System.out.println("Base64 Image: " + img);
            System.out.println("sitelink: " + sitelink);
            System.out.println("Title: " + title);
            System.out.println("Description: " + description);

            // 1. IMGBB 이미지 업로드 요청 준비
            RestTemplate restTemplate = new RestTemplate();
            String uploadUrl = IMGBB_API_URL + "?key=" + IMGBB_API_KEY;

            // 요청 본문 생성 (Multipart 요청)
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", img);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // API 요청
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(uploadUrl, requestEntity, Map.class);

            // 응답 확인
            Map<String, Object> response = responseEntity.getBody();
            if (response == null || !(Boolean) response.get("success")) {
                System.out.println("IMGBB 응답 실패: " + response);
                return new StatusDTO(500, "이미지 업로드 실패", null);
            }

            // 업로드된 이미지 URL 추출
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            String imgLink = (String) data.get("url");
            System.out.println("Uploaded Image URL: " + imgLink);

            // 2. support_site_info 테이블에 삽입
            helpInfoMapper.insertSiteInfo(title, description, sitelink,imgLink);
            return new StatusDTO(200, "사이트 정보 추가 성공", null);

        } catch (Exception e) {
            System.out.println("Exception 발생: " + e.getMessage());
            e.printStackTrace();
            return new StatusDTO(500, "사이트 정보 추가 실패: " + e.getMessage(), null);
        }
    }

}
