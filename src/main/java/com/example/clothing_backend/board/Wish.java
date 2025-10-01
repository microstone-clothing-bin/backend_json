package com.example.clothing_backend.board;

import com.example.clothing_backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "wish") // 실제 DB 테이블 이름: wish
@Getter
@Setter
@NoArgsConstructor // 기본 생성자 자동 생성
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 기본키 (자동 증가)
    private Long id;

    @Column(nullable = false)
    private Long userId; // 찜한 유저 ID (현재는 그냥 숫자 값으로 저장)

    @Column(nullable = false)
    private Long binId; // 찜한 옷 수거함 ID

    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성 시각 (수정 불가)

    @PrePersist
    protected void onCreate() {
        // 엔티티가 처음 저장될 때 자동으로 현재 시간 넣어줌
        this.createdAt = LocalDateTime.now();
    }

    // 유저 ID + 수거함 ID로 Wish 객체 생성
    public Wish(Long userId, Long binId) {
        this.userId = userId;
        this.binId = binId;
    }
}