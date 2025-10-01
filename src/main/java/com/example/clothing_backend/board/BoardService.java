package com.example.clothing_backend.board;

import com.example.clothing_backend.global.ImageUploadService; // VVV 전문가 import VVV
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; // VVV 추가 VVV
import java.io.IOException; // VVV 추가 VVV


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImageUploadService imageUploadService; // VVV 전문가 주입 VVV

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
                         MultipartFile imageFile, // VVV byte[] 대신 MultipartFile 받기 VVV
                         Double latitude, Double longitude) throws IOException { // VVV throws IOException 추가

        // VVV 여기가 핵심! 이미지 처리를 전문가에게 위임! VVV
        String imageUrl = imageUploadService.uploadImage(imageFile);

        Board board = new Board();
        board.setNickname(nickname);
        board.setTitle(title);
        board.setContent(content);
        board.setUserId(userId);
        board.setImageUrl(imageUrl); // VVV 이제 DB에는 URL만 저장! VVV
        board.setLatitude(latitude);
        board.setLongitude(longitude);
        board.setViewCnt(0);

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

