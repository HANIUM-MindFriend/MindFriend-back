package com.example.mindfriend.controller;

import com.example.mindfriend.domain.User;
import com.example.mindfriend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // 유저 단일 조회
    @GetMapping("/{userId}")
    public Optional<User> getUser(@PathVariable long userId) {
        return userService.getUser(userId);
    }
}
