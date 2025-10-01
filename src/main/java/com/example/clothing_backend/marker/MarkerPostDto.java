package com.example.clothing_backend.marker;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MarkerPostDto {
    private Long postId;          // 게시글 ID
    private String content;       // 게시글 내용
    private String imageUrl;      // 이미지 URL 저장
    private LocalDateTime createdAt; // 생성일
    private String authorNickname;   // 작성자 닉네임

    // ====== Entity -> DTO 변환 생성자 ======
    public MarkerPostDto(MarkerPost entity) {
        this.postId = entity.getPostId();
        this.content = entity.getContent();
        this.createdAt = entity.getCreatedAt();
        this.authorNickname = entity.getUser().getNickname(); // 작성자 닉네임 가져오기
        this.imageUrl = entity.getImageUrl(); // 이미지 URL
    }
}