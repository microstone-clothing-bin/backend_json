package com.example.clothing_backend.user;

import com.example.clothing_backend.global.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageUploadService imageUploadService;

    // 회원가입
    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // 로그인 시 사용자 조회
    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));
    }

    // 중복 확인
    public boolean isDuplicate(String type, String value) {
        if ("id".equals(type)) {
            return userRepository.existsById(value);
        } else if ("nickname".equals(type)) {
            return userRepository.existsByNickname(value);
        } else if ("email".equals(type)) {
            return userRepository.existsByEmail(value);
        }
        return false;
    }

    // 아이디 찾기
    public String findIdByNicknameAndEmail(String nickname, String email) {
        return userRepository.findByNicknameAndEmail(nickname, email)
                .map(User::getId)
                .orElse(null);
    }

    // 비밀번호 찾기 전 사용자 확인용 메소드
    public boolean verifyUserByIdAndEmail(String id, String email) {
        return userRepository.findByIdAndEmail(id, email).isPresent();
    }

    // 비밀번호 찾기
    public String findPwByIdAndEmail(String id, String email) {
        return userRepository.findByIdAndEmail(id, email)
                .map(User::getPassword)
                .orElse(null);
    }

    // 프로필 이미지 저장
    @Transactional
    public String saveProfileImage(MultipartFile file, String loginId) throws IOException {
        User user = getUser(loginId);
        if (user == null) {
            throw new UserNotFoundException("ID가 " + loginId + "인 사용자를 찾을 수 없습니다.");
        }

        String imageUrl = imageUploadService.uploadImage(file);
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(String id) {
        User user = getUser(id);
        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new UserNotFoundException("ID가 " + id + "인 사용자를 찾을 수 없습니다.");
        }
    }

    // 비밀번호 재설정
    @Transactional
    public void updatePassword(String id, String email, String newPassword) {
        User user = userRepository.findByIdAndEmail(id, email)
                .orElseThrow(() -> new UserNotFoundException("ID 또는 이메일이 일치하는 사용자를 찾을 수 없습니다."));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}