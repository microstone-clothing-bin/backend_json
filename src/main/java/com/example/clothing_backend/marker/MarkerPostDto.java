package com.example.clothing_backend.marker;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
// XXX 삭제 XXX: import java.util.Base64; // 더 이상 DTO에서 Base64 변환을 하지 않음

@Getter
@Setter
public class MarkerPostDto {
    private Long postId;
    private String content;
    private String imageUrl; // VVV 수정 VVV: imageBase64 -> imageUrl
    private LocalDateTime createdAt;
    private String authorNickname;

    // Entity -> DTO 변환을 위한 생성자
    public MarkerPostDto(MarkerPost entity) {
        this.postId = entity.getPostId();
        this.content = entity.getContent();
        this.createdAt = entity.getCreatedAt();
        this.authorNickname = entity.getUser().getNickname();

        // VVV 수정 VVV: getImage() 대신 getImageUrl()을 사용하고, Base64 변환 로직 제거
        this.imageUrl = entity.getImageUrl();
    }
}