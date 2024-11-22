package com.namu.helpInfo.controller;

import com.namu.common.annotation.RequiredRole;
import com.namu.common.dto.StatusDTO;
import com.namu.helpInfo.service.HelpInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/siteAdd")
    public ResponseEntity<StatusDTO> siteAdd(@RequestBody Map<String, String> requestBody) {

        logger.info("시작");
        String img = requestBody.get("img");
        logger.info(img);
        String link = requestBody.get("link");
        String title = requestBody.get("title");
        String description = requestBody.get("description");

        return ResponseEntity.ok(helpInfoService.addSiteInfo(img, link ,title, description));

    }
}
