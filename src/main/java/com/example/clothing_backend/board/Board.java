package com.example.clothing_backend.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String title;
    private String content;
    private String nickname;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime redate;

    private LocalDateTime modifiedAt;

    private Long userId;
    private int viewCnt;

    private Double latitude;
    private Double longitude;

    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(name = "image")
    private byte[] imageData;

    private String reviewText;

    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(name = "review_image")
    private byte[] reviewImage;

    @Transient
    private String imageBase64;

    @Transient
    private String reviewImageBase64;

    @PrePersist // INSERT 되기 전에 실행
    public void prePersist() {
        this.redate = LocalDateTime.now();
    }

    // VVV 추가 VVV: UPDATE 되기 전에 실행되는 콜백
    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now(); // 수정 시간 자동 저장
    }
}
