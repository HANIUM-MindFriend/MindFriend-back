package com.example.mindfriend.controller;

import com.example.mindfriend.common.response.result.ResultResponse;
import com.example.mindfriend.dto.response.getUser;

import com.example.mindfriend.security.SecurityUtils;
import com.example.mindfriend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import static com.example.mindfriend.common.response.result.ResultCode.GET_USER_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final SecurityUtils securityUtils;
    // 유저 단일 조회
    @GetMapping("/read")
    public ResultResponse<getUser> getUser() {
        getUser response = userService.getUser(securityUtils.getCurrentUserId());
        return new ResultResponse<>(GET_USER_SUCCESS, response);
    }
}
