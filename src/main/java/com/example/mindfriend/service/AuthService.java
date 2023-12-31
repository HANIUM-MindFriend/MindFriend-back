package com.example.mindfriend.service;

import com.example.mindfriend.common.response.exception.MindFriendBusinessException;
import com.example.mindfriend.domain.User;
import com.example.mindfriend.dto.request.signIn;
import com.example.mindfriend.dto.request.signUp;
import com.example.mindfriend.dto.response.*;
import com.example.mindfriend.repository.UserRepository;
import com.example.mindfriend.security.JwtTokenProvider;
import com.example.mindfriend.security.TokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.example.mindfriend.common.response.exception.ErrorCode.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3Uploader s3Uploader;

    // 회원가입
    public GetUser signUp(signUp request, MultipartFile profileImg) throws IOException {

        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            throw new MindFriendBusinessException(EMAIL_ALREADY_EXIST);
        }

        if (userRepository.existsByUserId(request.getUserId())) {
            throw new MindFriendBusinessException(ID_ALREADY_EXIST);
        }

        final String userProfileImg = s3Uploader.upload(profileImg, "images/users/");
        User user = request.toEntity(passwordEncoder, userProfileImg);
        userRepository.save(user);

        return GetUser.of(user);
    }

    // 로그인
    public TokenInfo signIn(signIn request) {

        if (!userRepository.existsByUserId(request.getUserId())) {
            throw new MindFriendBusinessException(USER_NOT_FOUND);
        }
        // 1. Login ID/PW 기반으로 Authentication 객체 생성
        // 이때 authentication는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUserId(), request.getUserPassword());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 메서드가 실행될 때 CustomUserDetailService에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        Optional<User> user = userRepository.findByUserId(request.getUserId());
        log.info("사용자 [" + user.get().getUserEmail() + "] 로그인 성공");
        log.info("사용자 [" + user.get().getUserEmail() + "] " + user.get().getAuthorities() + " 권한을 가지고 있습니다.");

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication, user.get().getUserIdx());

        return tokenInfo;
    }
}
