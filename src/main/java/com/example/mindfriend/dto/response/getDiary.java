package com.example.mindfriend.dto.response;

import com.example.mindfriend.domain.Diary;
import com.example.mindfriend.domain.User;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class getDiary {
    private Long diaryIdx;
    private Long userIdx;

    public static getDiary of(Diary diary) {
        return getDiary.builder()
                .diaryIdx(diary.getDiaryIdx())
                .userIdx(diary.getUser().getUserIdx())
                .build();
    }
}
