package com.example.clothing_backend.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
// XXX 삭제 XXX: import java.util.Base64; // 더 이상 DTO에서 Base64 변환을 하지 않음

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
    private String imageUrl; // VVV 수정 VVV: imageBase64 -> imageUrl
    private Double latitude;
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

        // VVV 수정 VVV: getImageData() 대신 getImageUrl()을 사용하고, Base64 변환 로직 제거
        this.imageUrl = board.getImageUrl();
    }
}

