package com.example.mindfriend.controller;

import com.example.mindfriend.common.response.result.ResultResponse;
import com.example.mindfriend.dto.request.*;
import com.example.mindfriend.dto.response.getUser;
import com.example.mindfriend.security.TokenInfo;
import com.example.mindfriend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.example.mindfriend.common.response.result.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    // 회원가입
    @PostMapping("/sign-up")
    public ResultResponse<getUser> signUp(@RequestBody signUp request) {
        getUser response = authService.signUp(request);
        return new ResultResponse<>(SIGN_UP_SUCCESS, response);
    }

    // 로그인
    @PostMapping("/sign-in")
    public ResultResponse<TokenInfo> signIn(@Valid @RequestBody signIn request) {
        TokenInfo response = authService.signIn(request);
        return new ResultResponse<>(SIGN_IN_SUCCESS, response);
    }
}
