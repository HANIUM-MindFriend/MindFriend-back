package com.example.mindfriend.service;

import com.example.mindfriend.domain.User;
import com.example.mindfriend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> getUser(long userId) {
        return userRepository.findById(userId);
    }
}
