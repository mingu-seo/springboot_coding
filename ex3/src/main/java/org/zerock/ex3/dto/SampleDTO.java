package org.zerock.ex3.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true) // 기존 빌더를 기반으로 새로운 객체 구성
public class SampleDTO {
    private Long sno;
    private String first;
    private String last;
    private LocalDateTime regTime;
    private int price;
    private Timestamp writeDate;
}
