package com.example.mindfriend.service;

import com.example.mindfriend.common.response.exception.UserNotFoundException;
import com.example.mindfriend.domain.User;
import com.example.mindfriend.dto.response.getUser;
import com.example.mindfriend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 유저 단건 조회
    public getUser getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return getUser.of(user);
    }
}
