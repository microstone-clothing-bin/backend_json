package com.example.clothing_backend.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;

    // 즐찾 추가
    @Transactional
    public void addWish(Long userId, Long binId) {
        Wish wish = new Wish(userId, binId); // 새로운 Wish 엔티티 생성
        wishRepository.save(wish); // DB 저장
    }

    // 즐찾 제거
    @Transactional
    public void removeWish(Long userId, Long binId) {
        wishRepository.deleteByUserIdAndBinId(userId, binId);
    }

    // 특정 유저의 즐찾 리스트 조회
    @Transactional(readOnly = true)
    public List<Wish> getUserWishes(Long userId) {
        return wishRepository.findByUserId(userId);
    }
}