package com.example.clothing_backend.marker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkerPostRepository extends JpaRepository<MarkerPost, Long> {

    // 의류수거함(bin) ID를 기준으로 모든 게시글을 최신순으로 찾아오는 메소드
    List<MarkerPost> findAllByClothingBin_IdOrderByCreatedAtDesc(Long binId);

    // 로그인 한 유저가 리뷰를 삭제하기 위한 메소드
    // @Query를 사용해 user.userId(Long)를 사용
    @Modifying
    @Query("DELETE FROM MarkerPost mp WHERE mp.user.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}