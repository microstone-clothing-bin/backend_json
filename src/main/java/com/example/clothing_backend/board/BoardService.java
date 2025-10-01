package com.example.clothing_backend.board;

import com.example.clothing_backend.global.ImageUploadService;
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
    private final ImageUploadService imageUploadService; // 이미지 업로드 처리 담당 (클라우디너리 사용중)

    // 게시글 리스트 조회 (페이징 지원)
    public Page<Board> getBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 특정 게시글 조회 + 조회수 증가
    @Transactional // 조회수 증가 때문에 쓰기 모드로 바꿔야 함
    public Board getBoard(long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + boardId));
        board.setViewCnt(board.getViewCnt() + 1); // 조회수 +1
        return board;
    }

    // 게시글 작성
    @Transactional
    public void addBoard(String nickname, String title, String content, Long userId,
                         MultipartFile imageFile, // 이미지 파일 업로드 받음
                         Double latitude, Double longitude) throws IOException {

        String imageUrl = imageUploadService.uploadImage(imageFile);

        Board board = new Board();
        board.setNickname(nickname);
        board.setTitle(title);
        board.setContent(content);
        board.setUserId(userId);
        board.setImageUrl(imageUrl); // DB에는 URL만 저장 (이미지 데이터 X)
        board.setLatitude(latitude);
        board.setLongitude(longitude);
        board.setViewCnt(0); // 새 글이니까 조회수 0으로 시작

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