package com.example.clothing_backend.marker;

import com.example.clothing_backend.global.ImageUploadService; // VVV 전문가 import VVV
import com.example.clothing_backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarkerPostService {

    private final MarkerPostRepository markerPostRepository;
    private final ClothingBinRepository clothingBinRepository;
    private final ImageUploadService imageUploadService; // VVV 전문가 주입 VVV

    // 특정 의류수거함의 모든 게시글을 DTO로 변환하여 조회
    public List<MarkerPostDto> getPostsByBinId(Long binId) {
        return markerPostRepository.findAllByClothingBin_IdOrderByCreatedAtDesc(binId)
                .stream()
                .map(MarkerPostDto::new) // DTO가 URL을 처리하도록 수정됨
                .collect(Collectors.toList());
    }

    // 새 게시글 생성
    @Transactional
    public void createPost(Long binId, User user, String content, MultipartFile imageFile) throws IOException {
        ClothingBin clothingBin = clothingBinRepository.findById(binId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 의류수거함 ID입니다: " + binId));

        // VVV 여기가 핵심! 이미지 처리를 전문가에게 위임! VVV
        String imageUrl = imageUploadService.uploadImage(imageFile);

        MarkerPost post = new MarkerPost();
        post.setContent(content);
        post.setClothingBin(clothingBin);
        post.setUser(user);
        post.setImageUrl(imageUrl); // 이제 DB에는 URL만 저장!

        markerPostRepository.save(post);
    }
}