package com.example.mindfriend.dto.response;

import com.example.mindfriend.domain.Diary;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class GetDiary {
    private Long diaryIdx;
    private Long userIdx;

    public static GetDiary of(Diary diary) {
        return GetDiary.builder()
                .diaryIdx(diary.getDiaryIdx())
                .userIdx(diary.getUser().getUserIdx())
                .build();
    }
}
