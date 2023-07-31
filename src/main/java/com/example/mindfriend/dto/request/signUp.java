package com.example.mindfriend.dto.request;

import com.example.mindfriend.domain.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class signUp {
    private String userId;
    private String userPassword;
    private String userNickname;
    private String userBirth;
    private String userEmail;
    private String userProfileImg;

    public User toEntity(PasswordEncoder passwordEncoder) {
        User user = User.builder()
                .userId(userId)
                .userEmail(userEmail)
                .userPassword(passwordEncoder.encode(userPassword))
                .userNickname(userNickname)
                .userBirth(userBirth)
                .userEmail(userEmail)
                .userProfileImg(userProfileImg)
                .build();

        return user;
    }
}
