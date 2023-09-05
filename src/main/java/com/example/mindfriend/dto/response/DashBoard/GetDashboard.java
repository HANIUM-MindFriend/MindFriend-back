package com.example.mindfriend.dto.response.DashBoard;

import com.example.mindfriend.domain.Diary;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class GetDashboard {

    GetEmoGraph graphStatistics;
    List<GetColorByMonth> moodTracker;

    public static GetDashboard of(int[] emotionGraph, List<Diary> diary) {
        GetEmoGraph graphStatistics = GetEmoGraph.of(emotionGraph);

        List<GetColorByMonth> colorByMonth = GetColorByMonth.of(diary);

        return GetDashboard.builder()
                .graphStatistics(graphStatistics)
                .moodTracker(colorByMonth)
                .build();
    }
}
