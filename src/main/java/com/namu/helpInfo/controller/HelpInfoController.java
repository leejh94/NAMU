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
    public ResponseEntity<StatusDTO> getInfoSiteList() {
        logger.info("infoSiteList");

        StatusDTO result = helpInfoService.getInfoSiteList();
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @RequiredRole({"ADMIN", "MASTER"}) // ADMIN, MASTER 권한만 접근 가능
    @PostMapping("/siteInfoAdd")
    public ResponseEntity<StatusDTO> siteInfoAdd(@RequestBody Map<String, String> requestBody) {
        String img = requestBody.get("img");
        String link = requestBody.get("link");
        String title = requestBody.get("title");
        String description = requestBody.get("description");

        StatusDTO result = helpInfoService.addSiteInfo(img, link, title, description);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @RequiredRole({"ADMIN", "MASTER"}) // ADMIN, MASTER 권한만 접근 가능
    @DeleteMapping("/deleteSiteInfo")
    public ResponseEntity<StatusDTO> deleteSiteInfo(@RequestParam("siteInfoId") int siteInfoId) {
        StatusDTO result = helpInfoService.deleteSiteInfo(siteInfoId);

        return ResponseEntity.status(result.getCode()).body(result);
    }

    @RequiredRole({"ADMIN", "MASTER"}) // ADMIN, MASTER 권한만 접근 가능
    @PostMapping("/saveSiteNewOrder")
    public ResponseEntity<StatusDTO> saveSiteNewOrder(@RequestBody List<Map<String, Object>> requestBody) {
        StatusDTO result = helpInfoService.saveSiteNewOrder(requestBody);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @RequiredRole({"ADMIN", "MASTER"}) // ADMIN, MASTER 권한만 접근 가능
    @PutMapping("/updateSiteInfo")
    public ResponseEntity<StatusDTO> updateSiteInfo(@RequestBody Map<String, String> requestBody) {

        int siteInfoId = Integer.parseInt(requestBody.get("siteInfoId"));
        String img = requestBody.get("img"); // Base64 이미지 (선택 사항)
        String link = requestBody.get("link");
        String title = requestBody.get("title");
        String description = requestBody.get("description");

        StatusDTO result = helpInfoService.updateSiteInfo(siteInfoId, img, link, title, description);
        return ResponseEntity.status(result.getCode()).body(result);

    }

    @GetMapping("/supportBoardList")
    public ResponseEntity<StatusDTO> getSupportBoardList(@RequestParam("region") String region) {
        logger.info("supportBoardList");
        StatusDTO response = helpInfoService.getSupportBoardList(region);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @RequiredRole({"ADMIN", "MASTER"})
    @PostMapping("/addSupportBoard")
    public ResponseEntity<StatusDTO> addSupportBoard(@RequestBody Map<String, String> requestBody) {
        String title = requestBody.get("title");
        String link = requestBody.get("link");
        String region = requestBody.get("region");

        StatusDTO response = helpInfoService.addSupportBoard(title, link, region);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @RequiredRole({"ADMIN", "MASTER"})
    @PutMapping("/updateSupportBoard")
    public ResponseEntity<StatusDTO> updateSupportBoard(@RequestBody Map<String, String> requestBody) {
        logger.info(requestBody.toString());

        int supportId = Integer.parseInt(requestBody.get("supportId"));
        String title = requestBody.get("title");
        String link = requestBody.get("link");
        String region = requestBody.get("region");

        StatusDTO response = helpInfoService.updateSupportBoard(supportId, title, link, region);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @RequiredRole({"ADMIN", "MASTER"})
    @DeleteMapping("/deleteSupportBoard")
    public ResponseEntity<StatusDTO> deleteSupportBoard(@RequestParam("supportId") int supportId) {
        logger.info(supportId + "");
        StatusDTO response = helpInfoService.deleteSupportBoard(supportId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

}