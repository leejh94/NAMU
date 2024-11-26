package com.namu.helpInfo.service;

import com.namu.common.dto.StatusDTO;
import com.namu.helpInfo.dto.SiteInfoDTO;
import com.namu.helpInfo.mapper.HelpInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private static final Logger logger = LoggerFactory.getLogger(HelpInfoService.class);

    @Autowired
    private HelpInfoMapper helpInfoMapper;

    private static final String IMGBB_API_URL = "https://api.imgbb.com/1/upload";

    @Value("${IMGBB_API_KEY}")
    private String imgbbApiKey;

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
            if (imgbbApiKey == null || imgbbApiKey.isEmpty()) {
                throw new IllegalStateException("IMGBB API 키가 설정되지 않았습니다.");
            }

            // 1. IMGBB 이미지 업로드 요청 준비
            RestTemplate restTemplate = new RestTemplate();
            String uploadUrl = IMGBB_API_URL + "?key=" + imgbbApiKey;

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

    /**
     * 사이트 정보 삭제
     * @param siteInfoId 삭제할 사이트 ID
     * @return StatusDTO 응답 데이터
     */
    public StatusDTO deleteSiteInfo(int siteInfoId) {
        try {
            logger.info("사이트 삭제 요청: siteInfoId={}", siteInfoId);

            helpInfoMapper.deleteSiteInfo(siteInfoId);
            logger.info("사이트 삭제 완료: siteInfoId={}", siteInfoId);

            return new StatusDTO(200, "사이트 삭제 성공", null);
        } catch (Exception e) {
            logger.error("사이트 삭제 실패: {}", e.getMessage(), e);
            return new StatusDTO(500, "사이트 삭제 실패: " + e.getMessage(), null);
        }
    }

    public StatusDTO saveSiteNewOrder(List<Map<String, Object>> siteOrderList) {
        try {
            for (Map<String, Object> item : siteOrderList) {
                int siteInfoId = (int) item.get("siteInfoId");
                int indexOrder = (int) item.get("indexOrder");

                // 매퍼 호출
                helpInfoMapper.updateSiteOrder(siteInfoId, indexOrder);
                logger.info("사이트 순서 업데이트 완료: siteInfoId={}, indexOrder={}", siteInfoId, indexOrder);
            }
            return new StatusDTO(200, "순서가 저장되었습니다.", null);
        } catch (Exception e) {
            logger.error("사이트 순서 업데이트 실패: {}", e.getMessage(), e);
            return new StatusDTO(500, "사이트 순서 업데이트 실패: " + e.getMessage(), null);
        }
    }

    public StatusDTO updateSiteInfo(int siteInfoId, String img, String link, String title, String description) {
        try {
            logger.info("사이트 수정 요청: siteInfoId={}, title={}, link={}, description={}", siteInfoId, title, link, description);

            String imgLink = null;

            // 이미지가 Base64 형태로 전달된 경우 업로드 처리
            if (img != null && !img.isEmpty()) {
                if (imgbbApiKey == null || imgbbApiKey.isEmpty()) {
                    throw new IllegalStateException("IMGBB API 키가 설정되지 않았습니다.");
                }

                // IMGBB 이미지 업로드 요청
                RestTemplate restTemplate = new RestTemplate();
                String uploadUrl = IMGBB_API_URL + "?key=" + imgbbApiKey;

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("image", img);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

                logger.info("IMGBB API 호출: {}", uploadUrl);
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(uploadUrl, requestEntity, Map.class);

                Map<String, Object> response = responseEntity.getBody();
                if (response == null || !(Boolean) response.get("success")) {
                    logger.error("IMGBB 응답 실패: {}", response);
                    return new StatusDTO(500, "이미지 업로드 실패", null);
                }

                // 업로드된 이미지 URL 추출
                Map<String, Object> data = (Map<String, Object>) response.get("data");
                imgLink = (String) data.get("url");
                logger.info("Uploaded Image URL: {}", imgLink);
            }

            // 기존 이미지 유지 시 imgLink는 null로 전달
            helpInfoMapper.updateSiteInfo(siteInfoId, title, description, link, imgLink);

            logger.info("사이트 정보 수정 완료: siteInfoId={}", siteInfoId);
            return new StatusDTO(200, "사이트 정보 수정 성공", null);
        } catch (Exception e) {
            logger.error("사이트 정보 수정 실패: {}", e.getMessage(), e);
            return new StatusDTO(500, "사이트 정보 수정 실패: " + e.getMessage(), null);
        }
    }



}
