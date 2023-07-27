package com.example.mindfriend.dto.response;

import com.example.mindfriend.domain.Diary;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class getDiaryDetail {
    private String profileImg;
    private Long diaryIdx;
    private String title;
    private LocalDateTime createdAt;
    private String content;
    private String image;

    public static getDiaryDetail of(Diary diary) {
        return getDiaryDetail.builder()
                .profileImg(diary.getUser().getUserProfileImg())
                .diaryIdx(diary.getDiaryIdx())
                .title(diary.getTitle())
                .createdAt(diary.getCreatedAt())
                .content(diary.getContent())
                .image(diary.getImage())
                .build();
    }
}
