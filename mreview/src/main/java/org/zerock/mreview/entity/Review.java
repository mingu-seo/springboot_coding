package org.zerock.mreview.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"movie","member"})
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewnum;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int grade;
    private String text;

    // 리뷰평점 수정
    public void changeGrade(int grade) {
        this.grade = grade;
    }
    // 리뷰내용 수정
    public void changeText(String text) {
        this.text = text;
    }
}
