package com.example.mindfriend.dto.response;

import com.example.mindfriend.domain.Diary;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class GetContentEmo {
    private Long diaryIdx;
    private int emoIdx;
    private String emotion;

    public static GetContentEmo of(Diary diary, int emoIdx, String emotion) {
        return GetContentEmo.builder()
                .diaryIdx(diary.getDiaryIdx())
                .emoIdx(emoIdx)
                .emotion(emotion)
                .build();
    }
}
