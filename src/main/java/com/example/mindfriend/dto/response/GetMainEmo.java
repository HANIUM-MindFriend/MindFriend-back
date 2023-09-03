package com.example.mindfriend.dto.response;

import com.example.mindfriend.domain.Diary;
import lombok.*;

import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class GetMainEmo {
    private Long diaryIdx;
    private int mainEmoIdx;
    private String mainEmo;

    public static GetMainEmo of(Optional<Diary> diary) {
        String mainEmo = diary.map(Diary::getMainEmotion).orElse("");
        int mainEmoIdx = 0;

        switch (mainEmo) {
            case "angry":
                mainEmoIdx = 1;
                break;
            case "disgust":
                mainEmoIdx = 2;
                break;
            case "fear":
                mainEmoIdx = 3;
                break;
            case "happiness":
                mainEmoIdx = 4;
                break;
            case "neutral":
                mainEmoIdx = 5;
                break;
            case "sadness":
                mainEmoIdx = 6;
                break;
            case "surprise":
                mainEmoIdx = 7;
                break;
            default:
                // 기본값 또는 오류 처리를 여기에 추가
                break;
        }
        return GetMainEmo.builder()
                .diaryIdx(diary.map(Diary::getDiaryIdx).orElse(null))
                .mainEmoIdx(mainEmoIdx)
                .mainEmo(mainEmo)
                .build();
    }
}
