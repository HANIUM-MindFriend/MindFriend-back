package com.example.mindfriend.dto.response.DashBoard;
import com.example.mindfriend.domain.Diary;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class GetColorByMonth {
    private Long diaryIdx;
    private LocalDateTime createdAt;
    private String mainEmotion;

    public static List<GetColorByMonth> of(List<Diary> diaries) {
        return diaries.stream()
                .map(diary -> GetColorByMonth.builder()
                        .diaryIdx(diary.getDiaryIdx())
                        .createdAt(diary.getCreatedAt())
                        .mainEmotion(diary.getMainEmotion())
                        .build())
                .collect(Collectors.toList());
    }
}
