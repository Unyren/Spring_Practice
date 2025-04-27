package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data

@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ReviewDTO {
    //review num
    private Long reviewnum;

    //Movie num
    private Long mno;

    //Member id
    private Long mid;

    //Member nickname
    private String nickname;

    //Member email
    private String email;

    //Review
    private int grade;
    private String text;
    private LocalDateTime regDate, modDate;
}
