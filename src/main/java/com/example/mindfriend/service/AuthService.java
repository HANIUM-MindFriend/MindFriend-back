package com.example.mindfriend.service;

import com.example.mindfriend.common.response.exception.MindFriendBusinessException;
import com.example.mindfriend.domain.User;
import com.example.mindfriend.dto.request.signUp;
import com.example.mindfriend.dto.response.*;
import com.example.mindfriend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.mindfriend.common.response.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public getUser signUp(signUp request) {

        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            throw new MindFriendBusinessException(EMAIL_ALREADY_EXIST);
        }

        if (userRepository.existsByUserId(request.getUserId())) {
            throw new MindFriendBusinessException(ID_ALREADY_EXIST);
        }

        User user = request.toEntity(passwordEncoder);
        userRepository.save(user);

        return getUser.of(user);
    }
}
