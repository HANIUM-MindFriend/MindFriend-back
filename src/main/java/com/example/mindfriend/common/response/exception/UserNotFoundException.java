package com.example.mindfriend.common.response.exception;

public class UserNotFoundException extends MindFriendBusinessException{
    public UserNotFoundException() {super(ErrorCode.USER_NOT_FOUND);}
}
