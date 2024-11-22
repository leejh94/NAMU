package com.namu.helpInfo.service;

import com.namu.common.dto.StatusDTO;
import com.namu.helpInfo.dto.SiteInfoDTO;
import com.namu.helpInfo.mapper.HelpInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(HelpInfoService.class);

    @Autowired
    private HelpInfoMapper helpInfoMapper;

    public StatusDTO getInfoSiteList() {
        try {
            logger.info("getInfoSiteList 호출됨.");
            List<SiteInfoDTO> siteInfoList = helpInfoMapper.getInfoSiteList();
            logger.info("목록 조회 성공: {}", siteInfoList);
            return new StatusDTO(200, "목록 조회 성공", siteInfoList);
        } catch (Exception e) {
            logger.error("목록 조회 실패: {}", e.getMessage(), e);
            return new StatusDTO(500, "목록 조회 실패: " + e.getMessage(), null);
        }
    }

    public StatusDTO addSiteInfo(String img, String sitelink, String title, String description) {
        try {

            // 1. IMGBB 이미지 업로드 요청 준비
            RestTemplate restTemplate = new RestTemplate();
            String uploadUrl = IMGBB_API_URL + "?key=" + IMGBB_API_KEY;

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", img);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // API 요청
            logger.info("IMGBB API 호출: {}", uploadUrl);
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(uploadUrl, requestEntity, Map.class);

            // 응답 확인
            Map<String, Object> response = responseEntity.getBody();
            if (response == null || !(Boolean) response.get("success")) {
                logger.error("IMGBB 응답 실패: {}", response);
                return new StatusDTO(500, "이미지 업로드 실패", null);
            }

            // 업로드된 이미지 URL 추출
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            String imgLink = (String) data.get("url");
            logger.info("Uploaded Image URL: {}", imgLink);

            // 2. support_site_info 테이블에 삽입
            helpInfoMapper.insertSiteInfo(title, description, sitelink, imgLink);
            logger.info("사이트 정보 추가 성공. Title: {}, Link: {}, Image URL: {}", title, sitelink, imgLink);
            return new StatusDTO(200, "사이트 정보 추가 성공", null);

        } catch (Exception e) {
            logger.error("사이트 정보 추가 실패: {}", e.getMessage(), e);
            return new StatusDTO(500, "사이트 정보 추가 실패: " + e.getMessage(), null);
        }
    }
}
