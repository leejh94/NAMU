package com.namu.board.controller;

import com.namu.board.service.BoardService;
import com.namu.common.annotation.RequiredRole;
import com.namu.common.dto.StatusDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.namu.common.util.JwtTokenUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity<StatusDTO> getPosts(
            @RequestParam("channelId") Long channelId,
            @RequestParam(value = "sortType", defaultValue = "recent") String sortType,
            @RequestParam(value = "page", defaultValue = "1") int page,  // 페이지 번호
            @RequestParam(value = "limit", defaultValue = "3") int limit) {  // 한 페이지에 보여줄 개수
        int offset = (page - 1) * limit;  // 페이지 기반으로 OFFSET 계산
        StatusDTO result = boardService.getPosts(channelId, sortType, offset, limit);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @RequiredRole({"ADMIN", "MASTER", "USER", "APPROVED_USER"})
    @PostMapping("/add")
    public ResponseEntity<StatusDTO> addPost(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        Long userId = JwtTokenUtil.extractUserIdFromToken(request.getHeader("Authorization"));
        Long channelId = Long.parseLong(requestBody.get("channelId"));
        String title = requestBody.get("title");
        String content = requestBody.get("content");

        StatusDTO result = boardService.addPost(userId, channelId, title, content);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @GetMapping("/detail")
    public ResponseEntity<StatusDTO> getPostDetail(@RequestParam("postId") Long postId) {
        StatusDTO result = boardService.getPostDetail(postId);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @RequiredRole({"ADMIN", "MASTER", "USER", "APPROVED_USER"})
    @PutMapping("/update")
    public ResponseEntity<StatusDTO> updatePost(
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {

        Long userId = JwtTokenUtil.extractUserIdFromToken(request.getHeader("Authorization"));
        Long postId = Long.parseLong(requestBody.get("postId"));
        String title = requestBody.get("title");
        String content = requestBody.get("content");

        StatusDTO result = boardService.updatePost(postId, userId, title, content);
        return ResponseEntity.status(result.getCode()).body(result);
    }


    @RequiredRole({"ADMIN", "MASTER", "USER", "APPROVED_USER"})
    @DeleteMapping("/delete")
    public ResponseEntity<StatusDTO> deletePost(@RequestParam("postId") Long postId, HttpServletRequest request) {

        Long userId = JwtTokenUtil.extractUserIdFromToken(request.getHeader("Authorization"));

        StatusDTO result = boardService.deletePost(postId, userId);
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @RequiredRole({"ADMIN", "MASTER", "USER", "APPROVED_USER"})
    @PostMapping("/recommend")
    public ResponseEntity<StatusDTO> recommendPost(
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {

        Long userId = JwtTokenUtil.extractUserIdFromToken(request.getHeader("Authorization"));
        Long postId = Long.parseLong(requestBody.get("postId"));

        StatusDTO result = boardService.recommendPost(postId, userId);
        return ResponseEntity.status(result.getCode()).body(result);
    }


}
