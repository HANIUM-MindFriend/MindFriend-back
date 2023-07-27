package com.example.mindfriend.common.response.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    // User
    GET_USER_SUCCESS(200, "U001", "유저 단건 조회하였습니다."),

    // Diary
    POST_DIARY_SUCCESS(200, "D001", "일기를 작성하였습니다."),
    GET_DIARY_SUCCESS(200, "D002", "일기 단건 조회하였습니다.");
    private final int status;
    private final String code;
    private final String message;
}
