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


    public User toEntity(PasswordEncoder passwordEncoder, String profileImg) {
        User user = User.builder()
                .userId(userId)
                .userEmail(userEmail)
                .userPassword(passwordEncoder.encode(userPassword))
                .userNickname(userNickname)
                .userProfileImg(profileImg)
                .userBirth(userBirth)
                .userEmail(userEmail)
                .build();

        return user;
    }
}
