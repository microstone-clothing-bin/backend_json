// 마커 DTO

package com.example.clothing_backend.marker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MarkerDto {

    private Long id;      // 의류 수거함 DB PK
    private double lat;   // 위도
    private double lng;   // 경도
    private String name;  // 수거함 이름 또는 주소
}