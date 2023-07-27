package com.example.mindfriend.service;

import com.example.mindfriend.common.response.exception.MindFriendBusinessException;
import com.example.mindfriend.domain.Diary;
import com.example.mindfriend.domain.User;
import com.example.mindfriend.dto.request.postDiary;
import com.example.mindfriend.dto.response.getDiary;
import com.example.mindfriend.dto.response.getDiaryDetail;
import com.example.mindfriend.repository.DiaryRepository;
import com.example.mindfriend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.mindfriend.common.response.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    // 일기 작성
    @Transactional
    public getDiary postDiary(Long userId, postDiary request) {

        if(request.getTitle() == null) {
            throw new MindFriendBusinessException(EMPTY_TITLE);
        }

        if(request.getContent() == null) {
            throw new MindFriendBusinessException(EMPTY_CONTENT);
        }

        User user = userRepository.getOne(userId);
        Diary diary = Diary.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .image(request.getImage())
                .build();

        Diary response = diaryRepository.save(diary);
        return getDiary.of(response);
    }

    // 일기 단건 조회
    public getDiaryDetail getDiaryDetail(long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new MindFriendBusinessException(DIARY_NOT_FOUND));
        return getDiaryDetail.of(diary);
    }
}
