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

    private String imageUrl;

    private String reviewText;
    private String reviewImageUrl;

    @PrePersist
    public void prePersist() {
        this.redate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
}