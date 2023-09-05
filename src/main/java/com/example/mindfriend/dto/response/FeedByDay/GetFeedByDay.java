package com.example.mindfriend.dto.response.FeedByDay;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class GetFeedByDay {
    private Long diaryIdx;
    private String mainEmotion;
    List<GetMainEmoList> mainEmoLists;
}
