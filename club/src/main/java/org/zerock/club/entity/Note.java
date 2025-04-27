package org.zerock.club.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Note extends BaseEntity {
    @Id//pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto_increment
    private Long num;

    private String title;
    private String content;

    @ManyToOne(fetch= FetchType.LAZY)//지연
    private ClubMember writer;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
