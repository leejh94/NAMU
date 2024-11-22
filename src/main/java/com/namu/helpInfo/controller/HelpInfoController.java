package com.namu.helpInfo.controller;

import com.namu.common.annotation.RequiredRole;
import com.namu.common.dto.StatusDTO;
import com.namu.helpInfo.service.HelpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/helpInfo")
public class HelpInfoController {

    // @RequiredRole({"MASTER"}) // 마스터 만
    // @RequiredRole({"ADMIN", "MASTER"})   // 관리자 만
    // @RequiredRole({"APPROVED_USER","ADMIN", "MASTER"}) // 관리자 + 인증 유저 만
    // @RequiredRole({"ADMIN", "MASTER", "USER", "APPROVED_USER"})  // 전체

    @Autowired
    private HelpInfoService helpInfoService;

    @GetMapping("/infoSiteList")
    public StatusDTO getInfoSiteList() {
        // 모든 사용자 가능
        System.out.println("호출");
        // Service에서 처리된 StatusDTO 반환
        return helpInfoService.getInfoSiteList();
    }


    @RequiredRole({"ADMIN", "MASTER"}) // ADMIN, MASTER 권한만 접근 가능
    @PostMapping("/siteAdd")
    public StatusDTO siteAdd(@RequestBody Map<String, String> requestBody) {
        String img = requestBody.get("img");
        String link = requestBody.get("link");
        String title = requestBody.get("title");
        String description = requestBody.get("description");

        return helpInfoService.addSiteInfo(img, link ,title, description);
    }
}