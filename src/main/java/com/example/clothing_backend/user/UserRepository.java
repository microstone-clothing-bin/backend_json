package com.example.clothing_backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 로그인 ID(String)로 사용자 조회
    Optional<User> findById(String id);

    // 아이디/닉네임 중복 확인
    boolean existsById(String id);
    boolean existsByNickname(String nickname);

    // 이메일 중복 확인을 위한 메소드
    boolean existsByEmail(String email);

    // 아이디 찾기
    Optional<User> findByNicknameAndEmail(String nickname, String email);

    // 비밀번호 찾기
    Optional<User> findByIdAndEmail(String id, String email);
}