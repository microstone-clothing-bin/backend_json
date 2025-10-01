package com.example.clothing_backend.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardDto {

    // 게시글 PK
    private Long boardId;
    // 제목
    private String title;
    // 내용
    private String content;
    // 작성자 닉네임
    private String nickname;
    // 작성일
    private LocalDateTime redate;
    // 수정일
    private LocalDateTime modifiedAt;
    // 조회수
    private int viewCnt;
    // 이미지 URL
    private String imageUrl;
    // 위치 위도
    private Double latitude;
    // 위치 경도
    private Double longitude;

    // Entity -> DTO 변환 생성자
    public BoardDto(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickname = board.getNickname();
        this.redate = board.getRedate();
        this.modifiedAt = board.getModifiedAt();
        this.viewCnt = board.getViewCnt();
        this.latitude = board.getLatitude();
        this.longitude = board.getLongitude();
        this.imageUrl = board.getImageUrl();
    }
}