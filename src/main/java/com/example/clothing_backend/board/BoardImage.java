package com.example.clothing_backend.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor

public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String imageUrl;    // 클라우드너리 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
    
    // 생성자 (Board Service)
    public BoardImage(String imageUrl, Board board) {
        this.imageUrl = imageUrl;
        this.board = board;
    }
}
