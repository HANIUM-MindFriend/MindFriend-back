package com.example.mindfriend.dto.response.FeedByDay;

import com.example.mindfriend.domain.Diary;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class GetMainEmoList {
    private String mainEmo;

    public static List<GetMainEmoList> of(List<Diary> diaries) {
        return diaries.stream()
                .map(diary -> GetMainEmoList.builder()
                        .mainEmo(diary.getMainEmotion())
                        .build())
                .collect(Collectors.toList());
    }
}
