package com.example.mindfriend.dto.response;

import com.example.mindfriend.domain.User;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class getUser {
    private String userId;
    private String userNickname;
    private String userEmail;
    private String userProfileImg;

    public static getUser of(User user) {
        return getUser.builder()
                .userId(user.getUserId())
                .userNickname(user.getUserNickname())
                .userEmail(user.getUserEmail())
                .userProfileImg(user.getUserProfileImg())
                .build();
    }
}
