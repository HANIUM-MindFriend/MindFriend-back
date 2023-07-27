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
    public static ResultResponse of(ResultCode resultCode, Object data) {
        return new ResultResponse(resultCode, data);
    }

    public static ResultResponse of(ResultCode resultCode) {
        return new ResultResponse(resultCode, "");
    }
}
