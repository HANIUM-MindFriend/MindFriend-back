package com.example.mindfriend.dto.request;

import com.example.mindfriend.domain.Diary;
import lombok.Data;

@Data
public class postDiary {
    private String title;
    private String content;

    public Diary toEntity(String postImg) {
        Diary diary = Diary.builder()
                .title(title)
                .content(content)
                .image(postImg)
                .build();
        return diary;
    }
}
