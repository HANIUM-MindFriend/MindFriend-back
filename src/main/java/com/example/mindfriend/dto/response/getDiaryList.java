package com.example.mindfriend.dto.response;

import com.example.mindfriend.domain.Diary;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class getDiaryList {
    private Long diaryIdx;

    public static List<getDiaryList> of(List<Diary> diaries) {
        return diaries.stream()
                .map(diary -> getDiaryList.builder().diaryIdx(diary.getDiaryIdx()).build())
                .collect(Collectors.toList());
    }
}
