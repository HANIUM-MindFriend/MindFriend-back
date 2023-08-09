package com.example.mindfriend.common.response.result;

import lombok.Getter;

@Getter
public class ResultResponse<T> {
    private int status;
    private String code;
    private String message;
    private T data;

    // 요청 성공한 경우
    public ResultResponse(ResultCode resultCode, T data) {
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    // 다음과 같이 제네릭 타입을 명시적으로 지정하도록 수정합니다.
    public static <T> ResultResponse<T> of(ResultCode resultCode, T data) {
        return new ResultResponse<>(resultCode, data);
    }

    // 마찬가지로 제네릭 타입을 명시적으로 지정하도록 수정합니다.
    public static <T> ResultResponse<T> of(ResultCode resultCode) {
        return new ResultResponse<>(resultCode, null); // 데이터가 없을 때는 null을 전달합니다.
    }
}
