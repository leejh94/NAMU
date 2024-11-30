package com.namu.board.service;

import com.namu.board.mapper.BoardMapper;
import com.namu.common.dto.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoardService {

    @Autowired
    private BoardMapper boardMapper;

    // 게시글 목록 조회
    public StatusDTO getPosts(Long channelId, String sortType, int offset, int limit) {
        try {
            List<Map<String, Object>> posts = boardMapper.getPostsByChannelId(channelId, sortType, offset, limit);
            return new StatusDTO(200, "목록 조회 성공", posts);
        } catch (Exception e) {
            return new StatusDTO(500, "목록 조회 실패: " + e.getMessage(), null);
        }
    }
    
    // 게시글 상세 조회
    public StatusDTO getPostDetail(Long postId) {
        try {
            Map<String, Object> post = boardMapper.getPostDetail(postId);
            if (post == null) {
                return new StatusDTO(404, "게시글이 존재하지 않습니다.", null);
            }
            return new StatusDTO(200, "게시글 조회 성공", post);
        } catch (Exception e) {
            return new StatusDTO(500, "게시글 조회 실패: " + e.getMessage(), null);
        }
    }

    // 게시글 추가
    public StatusDTO addPost(Long userId, Long channelId, String title, String content) {
        try {
            boardMapper.insertPost(userId, channelId, title, content);
            return new StatusDTO(201, "게시글이 성공적으로 추가되었습니다.", null);
        } catch (Exception e) {
            return new StatusDTO(500, "게시글 추가 실패: " + e.getMessage(), null);
        }
    }

    // 게시글 수정
    public StatusDTO updatePost(Long postId, Long userId, String title, String content) {
        try {
            Map<String, Object> post = boardMapper.getPostDetail(postId);

            if (post == null) {
                return new StatusDTO(404, "게시글이 존재하지 않습니다.", null);
            }

            // 작성자 검증
            Long authorId = (Long) post.get("userId");
            if (!authorId.equals(userId)) {
                return new StatusDTO(403, "작성자만 수정할 수 있습니다.", null);
            }

            boardMapper.updatePost(postId, title, content);
            return new StatusDTO(200, "게시글이 성공적으로 수정되었습니다.", null);
        } catch (Exception e) {
            return new StatusDTO(500, "게시글 수정 실패: " + e.getMessage(), null);
        }
    }

    // 게시글 삭제
    public StatusDTO deletePost(Long postId, Long userId) {
        try {
            Map<String, Object> post = boardMapper.getPostDetail(postId);
            if (post == null) {
                return new StatusDTO(404, "게시글이 존재하지 않습니다.", null);
            }
            // 작성자 검증
            Long authorId = (Long) post.get("userId");
            if (!authorId.equals(userId)) {
                return new StatusDTO(403, "작성자만 삭제할 수 있습니다.", null);
            }
            boardMapper.deletePost(postId);
            return new StatusDTO(200, "게시글이 성공적으로 삭제되었습니다.", null);
        } catch (Exception e) {
            return new StatusDTO(500, "게시글 삭제 실패: " + e.getMessage(), null);
        }
    }

    // 게시글 추천
    public StatusDTO recommendPost(Long postId, Long userId) {
        try {
            boolean alreadyRecommended = boardMapper.checkIfRecommended(postId, userId);
            if (alreadyRecommended) {
                return new StatusDTO(400, "이미 추천한 게시글입니다.", null);
            }

            boardMapper.insertPostRecommendation(postId, userId);
            boardMapper.incrementRecommendationCount(postId);
            return new StatusDTO(200, "게시글 추천 성공", null);
        } catch (Exception e) {
            return new StatusDTO(500, "게시글 추천 실패: " + e.getMessage(), null);
        }
    }
}
