package com.example.mindfriend.common.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Global
    INTERNAL_SERVER_ERROR(500, "G001", "내부 서버 오류입니다."),
    METHOD_NOT_ALLOWED(405, "G002", "허용되지 않은 HTTP method입니다."),
    INPUT_VALUE_INVALID(400, "G003", "유효하지 않은 입력입니다."),
    INPUT_TYPE_INVALID(400, "G004", "입력 타입이 유효하지 않습니다."),
    HTTP_MESSAGE_NOT_READABLE(400, "G005", "request message body가 없거나, 값 타입이 올바르지 않습니다."),
    HTTP_HEADER_INVALID(400, "G006", "request header가 유효하지 않습니다."),
    IMAGE_TYPE_NOT_SUPPORTED(400, "G007", "지원하지 않는 이미지 타입입니다."),
    FILE_CONVERT_FAIL(500, "G008", "변환할 수 없는 파일입니다."),
    ENTITY_TYPE_INVALID(500, "G009", "올바르지 않은 entity type 입니다."),
    FILTER_MUST_RESPOND(500, "G010", "필터에서 처리해야 할 요청이 Controller에 접근하였습니다."),

    // User
    USER_NOT_FOUND(404, "U001", "존재 하지 않는 사용자입니다."),

    // Diary
    DIARY_NOT_FOUND(404, "D003", "존재 하지 않는 다이어리입니다."),
    POST_DIARY_FAIL(400, "D004", "다이어리 작성에 실패했습니다."),
    POST_EMO_FAIL(400, "D005", "감정 추가에 실패했습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;

}
