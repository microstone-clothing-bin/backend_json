package com.example.clothing_backend.board;

import com.example.clothing_backend.marker.ClothingBin;
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
    private Long boardId; // DB가 알아서 ID 증가

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    // 작성자 닉네임
    private String nickname;

    // 작성자 프로필 이미지 URL
    private String profileImageUrl;

    @Column(columnDefinition = "TIMESTAMP")
    // 글 작성일
    private LocalDateTime redate;

    // 글 수정일
    private LocalDateTime modifiedAt;

    // 작성자 유저 ID
    private Long userId;

    // 조회수 카운트
    private int viewCnt;

    // 게시글 이밎 URL
    private String imageUrl;

    // 이 게시글과 연결된 의류수거함 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bin_id")    // DB에는 bin_id 컬럼으로 저장 BoardService에 작성
    
    // 위도 + 경도는 여기서 포함
    private ClothingBin clothingBin;

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