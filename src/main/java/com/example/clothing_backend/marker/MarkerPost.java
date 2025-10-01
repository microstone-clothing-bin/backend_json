package com.example.clothing_backend.marker;

import com.example.clothing_backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "marker_post") // DB 테이블 이름: marker_post
@Getter
@Setter
public class MarkerPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 기본키, auto_increment
    private Long postId;

    @Column(nullable = false)
    private String content; // 게시글 내용

    private String imageUrl; // 이미지 URL

    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성일 (수정 불가)

    // 관계 설정
    @ManyToOne(fetch = FetchType.LAZY) // Lazy 로딩 (필요할 때만 불러옴)
    @JoinColumn(name = "bin_id", nullable = false) // foreign key 컬럼명: bin_id
    private ClothingBin clothingBin; // 어느 옷 수거함에 달린 게시글인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 작성자 유저 정보

    @PrePersist
    public void prePersist() {
        // 엔티티가 처음 저장될 때 생성일 자동 세팅
        this.createdAt = LocalDateTime.now();
    }
}