package com.example.mindfriend.dto.request;

import lombok.Data;

@Data
public class PredictionRequest {
    private String sentence;

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
