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
    // 연결된 의류수거함 ID
    private Long binId;

    // 의류수거함 주소 추가
    private String roadAddress;

    // Entity -> DTO 변환 생성자
    public BoardDto(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickname = board.getNickname();
        this.redate = board.getRedate();
        this.modifiedAt = board.getModifiedAt();
        this.viewCnt = board.getViewCnt();

        // 단일 이미지 URL 로드
        this.imageUrl = board.getImageUrl();

        // board에 연결된 clothingBin 객체가 존재 시, 그 안에서 id와 좌표를 꺼내 DTO에 저장
        if (board.getClothingBin() != null) {
            this.binId = board.getClothingBin().getId();
            this.latitude = board.getClothingBin().getLatitude();
            this.longitude = board.getClothingBin().getLongitude();
            this.roadAddress = board.getClothingBin().getRoadAddress(); // 주소 값 복사
        }
    }
}