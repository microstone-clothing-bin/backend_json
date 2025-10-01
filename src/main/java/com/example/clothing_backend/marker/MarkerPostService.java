package com.example.clothing_backend.marker;

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

    // 특정 의류수거함의 모든 게시글을 DTO로 변환하여 조회
    public List<MarkerPostDto> getPostsByBinId(Long binId) {
        return markerPostRepository.findAllByClothingBin_IdOrderByCreatedAtDesc(binId)
                .stream()
                // [수정] new MarkerPostDto(post) 생성자에서 이미지 변환을 모두 처리하므로
                //         별도의 변환 로직이 필요 없음.
                .map(MarkerPostDto::new)
                .collect(Collectors.toList());
    }

    // 새 게시글 생성
    @Transactional
    public void createPost(Long binId, User user, String content, MultipartFile imageFile) throws IOException {
        ClothingBin clothingBin = clothingBinRepository.findById(binId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 의류수거함 ID입니다: " + binId));

        MarkerPost post = new MarkerPost();
        post.setContent(content);
        post.setClothingBin(clothingBin);
        post.setUser(user);

        if (imageFile != null && !imageFile.isEmpty()) {
            post.setImage(imageFile.getBytes());
        }

        markerPostRepository.save(post);
    }
}
