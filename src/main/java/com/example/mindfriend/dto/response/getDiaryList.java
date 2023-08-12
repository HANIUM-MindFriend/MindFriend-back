package com.example.mindfriend.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class getDiaryList {
    private Long diaryIdx;
}
