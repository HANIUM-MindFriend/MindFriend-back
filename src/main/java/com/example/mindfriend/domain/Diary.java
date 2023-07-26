package com.example.mindfriend.domain;

import com.example.mindfriend.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diaryIdx")
    private Long diaryIdx;

    // 제목
    @Column(name = "title")
    private String title;

    // 일기 내용
    @Column(name = "content")
    private String content;

    @Column(name = "image")
    private String image;

    @JoinColumn(name = "userId")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

}
