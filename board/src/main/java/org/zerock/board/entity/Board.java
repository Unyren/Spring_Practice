package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer")
public class Board extends BaseEntity {
    //pk 어노테이션
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long bno;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)//명시적으로 LAZY 로딩 지정
    private Member writer; //연관관계 지정

    //게시글 수정지 제목과 내용만 수정
    public void chageTitle(String title) {
        this.title = title;
    }
    public void chageContent(String content) {
        this.content = content;
    }
}
