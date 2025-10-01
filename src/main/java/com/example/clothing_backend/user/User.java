package com.example.clothing_backend.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "`user`")
@Getter
@Setter
public class User { // XXX 삭제 XXX: implements UserDetails

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

    @JdbcTypeCode(SqlTypes.VARBINARY)
    private byte[] profileImageBlob;

    @Transient
    private String profileImageBase64;

    /*
    XXX 삭제 XXX
    Role 엔티티를 삭제했으므로, 더 이상 User와 Role의 관계는 필요 없음.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    */

    @PrePersist
    public void prePersist() {
        this.redate = LocalDateTime.now();
    }

    /*
    XXX 삭제 XXX
    UserDetails 인터페이스를 구현하지 않으므로, 아래의 모든 Override 메소드는 필요 없음.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { ... }
    @Override
    public String getUsername() { ... }
    @Override
    public boolean isAccountNonExpired() { ... }
    @Override
    public boolean isAccountNonLocked() { ... }
    @Override
    public boolean isCredentialsNonExpired() { ... }
    @Override
    public boolean isEnabled() { ... }
    */
}
