package com.example.clothing_backend.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Base64;

@Getter
@NoArgsConstructor
public class BoardDto {
    private Long boardId;
    private String title;
    private String content;
    private String nickname;
    private LocalDateTime redate;
    private LocalDateTime modifiedAt;
    private int viewCnt;
    private String imageBase64;
    private Double latitude;  // VVV 추가 VVV: 위도
    private Double longitude; // VVV 추가 VVV: 경도

    // Entity -> DTO 변환 생성자
    public BoardDto(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickname = board.getNickname();
        this.redate = board.getRedate();
        this.modifiedAt = board.getModifiedAt();
        this.viewCnt = board.getViewCnt();
        this.latitude = board.getLatitude();   // VVV 추가 VVV
        this.longitude = board.getLongitude(); // VVV 추가 VVV
        if (board.getImageData() != null) {
            this.imageBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(board.getImageData());
        }
    }
}

