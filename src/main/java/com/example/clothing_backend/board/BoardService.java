package com.example.clothing_backend.board;

import com.example.clothing_backend.global.ImageUploadService;
import com.example.clothing_backend.marker.ClothingBin;
import com.example.clothing_backend.marker.ClothingBinRepository;
import com.example.clothing_backend.user.User;
import com.example.clothing_backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; // 이미지 업로드 받을 때 사용
import java.io.IOException; // 업로드 실패시 예외 처리

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository; // DB 접근용 리포지토리
    private final ImageUploadService imageUploadService; // 이미지 업로드 처리 담당
    private final ClothingBinRepository clothingBinRepository; // 의류수거함 정보 조회용
    private final UserRepository userRepository; // 사용자 정보 조회를 위해 주입

    // 게시글 리스트 조회
    public Page<Board> getBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 특정 게시글 조회 + 조회수 증가
    @Transactional
    public Board getBoard(long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + boardId));
        board.setViewCnt(board.getViewCnt() + 1); // 조회수 +1
        return board;
    }

    // 게시글 작성
    @Transactional
    public void addBoard(String nickname, String title, String content, Long userId,
                         MultipartFile imageFile,
                         Long binId) throws IOException {

        // userId로 User 객체를 찾아서 프로필 이미지 URL 확보
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        Board board = new Board();
        board.setNickname(nickname); // user.getNickname()을 쓰는 게 더 정확할 수 있음
        board.setTitle(title);
        board.setContent(content);
        board.setUserId(userId);

        // User 객체에서 프로필 이미지 URL을 꺼내 board에 저장
        board.setProfileImageUrl(user.getProfileImageUrl());

        board.setViewCnt(0); // 새 글이니까 조회수 0으로 시작

        // 이미지 업로드 로직
        String imageUrl = imageUploadService.uploadImage(imageFile);
        board.setImageUrl(imageUrl);

        // binId로 ClothingBin을 찾아서 연결
        if (binId != null) {
            ClothingBin bin = clothingBinRepository.findById(binId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 의류수거함 ID입니다: " + binId));
            board.setClothingBin(bin);
        }

        boardRepository.save(board); // DB 저장
    }

    // 게시글 수정 (텍스트만 수정 가능)
    @Transactional
    public void updateBoardTextOnly(long boardId, String title, String content, Long loginUserId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + boardId));

        // 작성자 본인만 수정 가능
        if (!board.getUserId().equals(loginUserId)) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        board.setTitle(title);
        board.setContent(content);
    }

    // 게시글 삭제 (관리자나 권한 체크 없는 버전)
    @Transactional
    public void deleteBoard(long boardId) {
        boardRepository.deleteById(boardId);
    }

    // 게시글 삭제 (작성자 본인 확인 후 삭제)
    @Transactional
    public void deleteBoard(Long userId, long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + boardId));

        if (!board.getUserId().equals(userId)) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }

        boardRepository.delete(board);
    }
}