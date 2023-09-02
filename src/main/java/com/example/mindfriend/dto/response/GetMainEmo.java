package com.example.mindfriend.dto.response;

import com.example.mindfriend.domain.Diary;
import lombok.*;

import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class GetMainEmo {
    private Long diaryIdx;
    private int mainEmo;

    public static GetMainEmo of(Optional<Diary> diary, int mainEmo) {
        return GetMainEmo.builder()
                .diaryIdx(diary.get().getDiaryIdx())
                .mainEmo(mainEmo)
                .build();
    }
}
