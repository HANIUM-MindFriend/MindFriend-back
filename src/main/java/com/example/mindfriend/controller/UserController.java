package com.example.mindfriend.controller;

import com.example.mindfriend.common.response.result.ResultResponse;
import com.example.mindfriend.dto.response.getUser;
import com.example.mindfriend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.example.mindfriend.common.response.result.ResultCode.GET_USER_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    // 유저 단일 조회
    @GetMapping("/{userId}")
    public ResultResponse<getUser> getUser(@PathVariable long userId) {
        getUser response = userService.getUser(userId);
        return new ResultResponse<>(GET_USER_SUCCESS, response);
    }
}
