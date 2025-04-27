package org.zerock.mreview.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewnum;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int grade;//평점
    private String text;//리뷰 내용

    public void changeGrade(int grade) {
        this.grade = grade;
    }
    public void changeText(String text) {
        this.text = text;
    }
}
