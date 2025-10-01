package com.example.clothing_backend.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// XXX 삭제 XXX: import javax.swing.*; // 서버에 필요 없는 데스크탑 UI 라이브러리

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<Board> getBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Board getBoard(long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + boardId));
        board.setViewCnt(board.getViewCnt() + 1);
        return board;
    }

    @Transactional
    public void addBoard(String nickname, String title, String content, Long userId,
                         byte[] imageData, Double latitude, Double longitude) {
        Board board = new Board();
        board.setNickname(nickname);
        board.setTitle(title);
        board.setContent(content);
        board.setUserId(userId);
        board.setImageData(imageData);
        board.setLatitude(latitude);
        board.setLongitude(longitude);
        board.setViewCnt(0);
        // XXX 삭제 XXX: board.setRedate(LocalDateTime.now()); // Board Entity의 @PrePersist가 자동으로 처리

        boardRepository.save(board);
    }

    @Transactional
    public void updateBoardTextOnly(long boardId, String title, String content, Long loginUserId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + boardId));

        if (!board.getUserId().equals(loginUserId)) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }
        board.setTitle(title);
        board.setContent(content);
        // XXX 삭제 XXX: board.setModifiedAt(LocalDateTime.now()); // Board Entity의 @PreUpdate가 자동으로 처리
        // boardRepository.save(board)가 호출될 때 @PreUpdate가 동작함
    }

    @Transactional
    public void deleteBoard(long boardId) {
        boardRepository.deleteById(boardId);
    }

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