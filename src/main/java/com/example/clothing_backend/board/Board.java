package com.example.clothing_backend.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
public class Board {

    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    // DB가 알아서 조회수 증가
    private Long boardId;
    // 게시글 제목
    private String title;
    // 게시글 내용
    private String content;
    // 작성자 닉네임
    private String nickname;

    @Column(columnDefinition = "TIMESTAMP")
    // 글 작성일
    private LocalDateTime redate;

    // 글 수정일
    private LocalDateTime modifiedAt;

    // 작성자 유저 ID
    private Long userId;

    // 조회수 카운트
    private int viewCnt;

    // 위치 정보 (위도)
    private Double latitude;
    // 위치 정보 (경도)
    private Double longitude;

    // 게시글 이미지 URL
    private String imageUrl;

    // 리뷰 텍스트
    private String reviewText;
    // 리뷰 이미지 URL
    private String reviewImageUrl;

    @PrePersist
    public void prePersist() {
        this.redate = LocalDateTime.now(); // 작성일 현재 시각으로 세팅
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now(); // 수정일 현재 시각으로 세팅
    }
}