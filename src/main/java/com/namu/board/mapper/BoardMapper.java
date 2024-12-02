package com.namu.board.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {

    // 게시글 목록 조회
    List<Map<String, Object>> getPostsByChannelId(
            @Param("channelId") Long channelId,
            @Param("sortType") String sortType,
            @Param("offset") int offset,
            @Param("limit") int limit);

    // 게시글 상세 조회
    Map<String, Object> getPostDetail(@Param("postId") Long postId);

    // 게시글 추가
    void insertPost(@Param("userId") Long userId,
                    @Param("channelId") Long channelId,
                    @Param("title") String title,
                    @Param("content") String content,
                    @Param("isAdmin") boolean isAdmin);

    // 게시글 수정
    void updatePost(@Param("postId") Long postId,
                    @Param("title") String title,
                    @Param("content") String content);

    // 게시글 삭제
    void deletePost(@Param("postId") Long postId);

    // 게시글 추천 여부 확인
    boolean checkIfRecommended(@Param("postId") Long postId, @Param("userId") Long userId);

    // 게시글 추천 추가
    void insertPostRecommendation(@Param("postId") Long postId, @Param("userId") Long userId);

    // 추천수 증가
    void incrementRecommendationCount(@Param("postId") Long postId);
}
