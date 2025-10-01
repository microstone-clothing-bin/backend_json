package com.example.clothing_backend.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "`user`") // user는 DB 예약어이므로 백틱(`) 처리
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // PK, auto_increment

    @Column(unique = true, nullable = false)
    private String id; // 로그인용 아이디, 중복 불가

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호

    @Column(unique = true, nullable = false)
    private String nickname; // 닉네임, 중복 불가

    @Column(nullable = false)
    private String email; // 이메일, 중복 불가

    private LocalDateTime redate; // 계정 생성 시각

    private String profileImageUrl; // 프로필 이미지 URL

    @PrePersist
    public void prePersist() {
        this.redate = LocalDateTime.now(); // 엔티티 저장 직전에 생성 시간 자동 설정
    }
}