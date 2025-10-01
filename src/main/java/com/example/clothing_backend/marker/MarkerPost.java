package com.example.clothing_backend.marker;

import com.example.clothing_backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
// XXX 삭제 XXX: import org.hibernate.annotations.JdbcTypeCode;
// XXX 삭제 XXX: import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "marker_post")
@Getter
@Setter
public class MarkerPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String content;

    // VVV 수정 VVV: byte[] 대신 이미지 URL을 저장할 String 타입으로 변경
    private String imageUrl;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    // --- 관계 설정 ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bin_id", nullable = false)
    private ClothingBin clothingBin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
