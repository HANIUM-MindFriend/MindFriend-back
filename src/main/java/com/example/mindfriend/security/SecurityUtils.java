package com.example.mindfriend.security;

import com.example.mindfriend.common.response.exception.ErrorCode;
import com.example.mindfriend.common.response.exception.MindFriendBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityUtils {
    public static String getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new MindFriendBusinessException(ErrorCode.GET_USER_FAILED);
        }
        return authentication.getName();
    }
}
