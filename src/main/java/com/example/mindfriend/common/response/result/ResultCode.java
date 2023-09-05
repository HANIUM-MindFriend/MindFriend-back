package com.example.mindfriend.common.response.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    //DuplicateUser
    DUPLICATE_USER_SUCCESS(200, "U001", "유저 id 중복 확인하였습니다."),
    // User
    GET_USER_SUCCESS(200, "U002", "유저 단건 조회하였습니다."),

    // Auth
    SIGN_UP_SUCCESS(200, "A001", "회원가입에 성공하였습니다."),
    SIGN_IN_SUCCESS(200, "A002", "로그인에 성공하였습니다."),

    // Diary
    POST_DIARY_SUCCESS(200, "D001", "일기를 작성하였습니다."),
    GET_DIARY_SUCCESS(200, "D002", "일기 단건 조회하였습니다."),
    GET_DIARYLIST_SUCESS(200, "D003", "일기 리스트를 조회하였습니다"),
    DELETE_DIARY_SUCCESS(200, "D004", "일기를 삭제하였습니다."),
    GET_MAIN_EMOTION_SUCCEESS(200, "D005", "메인 감정을 조회하였습니다."),
    GET_GPT_SUCCESS(200, "D006", "챗봇 조회하였습니다."),
    GET_DASHBOARD_SUCCESS(200, "D007", "개인일기 관리 대시보드를 조회하였습니다.")
    ;
    private final int status;
    private final String code;
    private final String message;
}
