package com.example.mindfriend.common.response.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class MindFriendBusinessException extends RuntimeException {

    private ErrorCode errorCode;

    private List<ErrorResponse.FieldError> errors = new ArrayList<>();

    public MindFriendBusinessException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public MindFriendBusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
