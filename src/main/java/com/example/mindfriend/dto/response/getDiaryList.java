package com.example.mindfriend.dto.response;

import com.example.mindfriend.domain.Diary;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class getDiaryList {
    private Long diaryIdx;
    private LocalDateTime date;

    public static List<getDiaryList> of(List<Diary> diaries) {
        return diaries.stream()
                .map(diary -> getDiaryList.builder()
                        .diaryIdx(diary.getDiaryIdx())
                        .date(diary.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
