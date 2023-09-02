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
    private int fear;

    @Column(name = "surprise")
    private int surprise;

    @Column(name = "angry")
    private int angry;

    @Column(name = "sadness")
    private int sadness;

    @Column(name = "neutral")
    private int neutral;

    @Column(name = "happiness")
    private int happiness;

    @Column(name = "disgust")
    private int disgust;

    @Column(name = "mainEmotion")
    private int mainEmotion;

    @Builder
    public Diary(User user, String title, String content, String image) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.image = image;
    }

    // == 상태 변경 메서드 == //
    public String increaseEmo(int emotionType) {
        String increasedColumn = null; // 증가한 컬럼 이름을 저장할 변수 초기화

        switch (emotionType) {
            case 1:
                this.angry++;
                increasedColumn = "angry"; // angry 증가
                break;
            case 2:
                this.disgust++;
                increasedColumn = "disgust"; // disgust 증가
                break;
            case 3:
                this.fear++;
                increasedColumn = "fear"; // fear 증가
                break;
            case 4:
                this.happiness++;
                increasedColumn = "happiness"; // happiness 증가
                break;
            case 5:
                this.neutral++;
                increasedColumn = "neutral"; // neutral 증가
                break;
            case 6:
                this.sadness++;
                increasedColumn = "sadness"; // sadness 증가
                break;
            default:
                this.surprise++;
                increasedColumn = "surprise"; // surprise 증가
                break;
        }
        return increasedColumn; // 증가한 컬럼 이름 반환
    }

}
