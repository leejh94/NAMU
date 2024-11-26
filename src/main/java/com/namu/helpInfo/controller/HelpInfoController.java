package com.namu.helpInfo.controller;

import com.namu.common.annotation.RequiredRole;
import com.namu.common.dto.StatusDTO;
import com.namu.helpInfo.service.HelpInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/helpInfo")
public class HelpInfoController {

    private static final Logger logger = LoggerFactory.getLogger(HelpInfoController.class);

    // @RequiredRole({"MASTER"}) // 마스터 만
    // @RequiredRole({"ADMIN", "MASTER"})   // 관리자 만
    // @RequiredRole({"APPROVED_USER","ADMIN", "MASTER"}) // 관리자 + 인증 유저 만
    // @RequiredRole({"ADMIN", "MASTER", "USER", "APPROVED_USER"})  // 전체

    @Autowired
    private HelpInfoService helpInfoService;

    @GetMapping("/infoSiteList")
    public StatusDTO getInfoSiteList() {
        // 모든 사용자 가능
        // Service에서 처리된 StatusDTO 반환
        return helpInfoService.getInfoSiteList();
    }


    @RequiredRole({"ADMIN", "MASTER"}) // ADMIN, MASTER 권한만 접근 가능
    @PostMapping("/siteInfoAdd")
    public ResponseEntity<StatusDTO> siteInfoAdd(@RequestBody Map<String, String> requestBody) {

        String img = requestBody.get("img");
        String link = requestBody.get("link");
        String title = requestBody.get("title");
        String description = requestBody.get("description");

        return ResponseEntity.ok(helpInfoService.addSiteInfo(img, link ,title, description));

    }

    /**
     * 사이트 정보 삭제 API
     * @param siteInfoId 삭제 요청 데이터
     * @return StatusDTO 응답 데이터
     */
    @RequiredRole({"ADMIN", "MASTER"}) // ADMIN, MASTER 권한만 접근 가능
    @DeleteMapping("/deleteSiteInfo")
    public ResponseEntity<StatusDTO> deleteSiteInfo(@RequestParam("siteInfoId") int siteInfoId) {
        try {
            return ResponseEntity.ok(helpInfoService.deleteSiteInfo(siteInfoId));
        } catch (Exception e) {
            logger.error("사이트 삭제 요청 처리 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(
                    new StatusDTO(500, "사이트 삭제 요청 처리 실패: " + e.getMessage(), null)
            );
        }
    }

    @RequiredRole({"ADMIN", "MASTER"}) // ADMIN, MASTER 권한만 접근 가능
    @PostMapping("/saveSiteNewOrder")
    public ResponseEntity<StatusDTO> saveSiteNewOrder(@RequestBody List<Map<String, Object>> requestBody) {

        return ResponseEntity.ok(helpInfoService.saveSiteNewOrder(requestBody));
    }

    @RequiredRole({"ADMIN", "MASTER"}) // ADMIN, MASTER 권한만 접근 가능
    @PutMapping("/updateSiteInfo")
    public ResponseEntity<StatusDTO> updateSiteInfo(@RequestBody Map<String, String> requestBody) {
        try {
            int siteInfoId = Integer.parseInt(requestBody.get("siteInfoId"));
            String img = requestBody.get("img"); // Base64 이미지 (선택 사항)
            String link = requestBody.get("link");
            String title = requestBody.get("title");
            String description = requestBody.get("description");

            return ResponseEntity.ok(helpInfoService.updateSiteInfo(siteInfoId, img, link, title, description));
        } catch (Exception e) {
            logger.error("사이트 수정 요청 처리 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(
                    new StatusDTO(500, "사이트 수정 요청 처리 실패: " + e.getMessage(), null)
            );
        }
    }


}
