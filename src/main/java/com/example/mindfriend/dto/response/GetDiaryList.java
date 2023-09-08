package com.example.mindfriend.dto.response;

import com.example.mindfriend.domain.Diary;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class GetDiaryList {
    private Long diaryIdx;
    private LocalDate date;

    public static List<GetDiaryList> of(List<Diary> diaries) {
        return diaries.stream()
                .map(diary -> GetDiaryList.builder()
                        .diaryIdx(diary.getDiaryIdx())
                        .date(diary.getCreatedAt().toLocalDate())
                        .build())
                .collect(Collectors.toList());
    }
}
