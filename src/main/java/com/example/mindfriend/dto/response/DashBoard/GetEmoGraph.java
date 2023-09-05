package com.example.mindfriend.dto.response.DashBoard;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class GetEmoGraph {
    private int angry;
    private int disgust;
    private int fear;
    private int happiness;
    private int neutral;
    private int sadness;
    private int surprise;

    public static GetEmoGraph of(int[] emotionArray) {

        return GetEmoGraph.builder()
                .angry(emotionArray[0])
                .disgust(emotionArray[1])
                .fear(emotionArray[2])
                .happiness(emotionArray[3])
                .neutral(emotionArray[4])
                .sadness(emotionArray[5])
                .surprise(emotionArray[6])
                .build();
    }
}
