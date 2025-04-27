package org.zerock.mreview.sample;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

//ex) 화면에서 '2018-01-01' 과 같이 문자열로 전달된 데이터를 java.util.Date 타입으로 변환하는 작업.

@Data
public class TodoDTO {

    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd ")
    private Date dueDate;

}
