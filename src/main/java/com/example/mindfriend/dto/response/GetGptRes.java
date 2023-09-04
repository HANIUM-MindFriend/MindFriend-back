package com.example.mindfriend.dto.response;

import com.example.mindfriend.domain.Diary;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class GetGptRes {
    private Long diaryIdx;
    private String chatbot;

    public static GetGptRes of(Diary diary, String chatbot) {
        return GetGptRes.builder()
                .diaryIdx(diary.getDiaryIdx())
                .chatbot(chatbot)
                .build();
    }
}
