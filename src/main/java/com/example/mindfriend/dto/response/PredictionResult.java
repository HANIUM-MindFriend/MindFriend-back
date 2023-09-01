package com.example.mindfriend.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PredictionResult {
    private String predictedEmotion;

    public PredictionResult(String predictedEmotion) {
        this.predictedEmotion = predictedEmotion;
    }

    public String getPredictedEmotion() {
        return predictedEmotion;
    }

    public void setPredictedEmotion(String predictedEmotion) {
        this.predictedEmotion = predictedEmotion;
    }
}