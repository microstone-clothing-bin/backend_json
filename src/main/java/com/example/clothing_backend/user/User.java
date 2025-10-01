package com.example.clothing_backend.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
// XXX 삭제 XXX: import org.hibernate.annotations.JdbcTypeCode;
// XXX 삭제 XXX: import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "`user`")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    private LocalDateTime redate;

    // VVV 수정 VVV: byte[] 대신 이미지 URL을 저장할 String 타입으로 변경
    private String profileImageUrl;

    // XXX 삭제 XXX: Base64 관련 필드는 이제 필요 없음
    // @Transient
    // private String profileImageBase64;


    @PrePersist
    public void prePersist() {
        this.redate = LocalDateTime.now();
    }
}

