package com.example.clothing_backend.marker;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Base64; // Base64 인코딩을 위해 import 추가

@Getter
@Setter
public class MarkerPostDto {
    private Long postId;
    private String content;
    private String imageBase64;
    private LocalDateTime createdAt;
    private String authorNickname; // 작성자 닉네임만 포함

    // Entity -> DTO 변환을 위한 생성자
    public MarkerPostDto(MarkerPost entity) {
        this.postId = entity.getPostId();
        this.content = entity.getContent();
        this.createdAt = entity.getCreatedAt();
        this.authorNickname = entity.getUser().getNickname(); // User 객체에서 닉네임만 가져옴

        // VVV 추가 VVV: byte[] 이미지 데이터가 있으면 Base64 문자열로 변환해서 저장
        if (entity.getImage() != null) {
            this.imageBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(entity.getImage());
        }
    }
}
