package com.example.mindfriend.domain;

import com.example.mindfriend.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
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

    @Column(name = "emotion")
    private String emotion;

    @JsonIgnore
    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @Column(name = "fear")
    private Long fear;

    @Column(name = "surprise")
    private Long surprise;

    @Column(name = "angry")
    private Long angry;

    @Column(name = "sadness")
    private Long sadness;

    @Column(name = "neutral")
    private Long neutral;

    @Column(name = "happiness")
    private Long happiness;

    @Column(name = "disgust")
    private Long disgust;

    @Column(name = "mainEmotion")
    private Long mainEmotion;


    @Builder
    public Diary(User user, String title, String content, String image) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.image = image;
    }
}
