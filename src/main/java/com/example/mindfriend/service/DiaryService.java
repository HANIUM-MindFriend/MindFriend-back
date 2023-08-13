package com.example.mindfriend.service;

import com.example.mindfriend.common.response.exception.MindFriendBusinessException;
import com.example.mindfriend.common.response.exception.UserNotFoundException;
import com.example.mindfriend.domain.Diary;
import com.example.mindfriend.domain.User;
import com.example.mindfriend.dto.request.postDiary;
import com.example.mindfriend.dto.request.postDiaryEmo;
import com.example.mindfriend.dto.response.getDiary;
import com.example.mindfriend.dto.response.getDiaryDetail;
import com.example.mindfriend.dto.response.getDiaryList;
import com.example.mindfriend.repository.DiaryRepository;
import com.example.mindfriend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.mindfriend.common.response.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    // 일기 작성
    @Transactional
    public getDiary postDiary(String userId, postDiary request) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        Diary diary = Diary.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .image(request.getImage())
                .build();

        Diary response = diaryRepository.save(diary);
        if (response == null) {
            throw new MindFriendBusinessException(POST_DIARY_FAIL);
        }
        return getDiary.of(response);
    }

    // 일기 단건 조회
    public getDiaryDetail getDiaryDetail(long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new MindFriendBusinessException(DIARY_NOT_FOUND));
        return getDiaryDetail.of(diary);
    }

    // 일기 감정 수정(추가)
    public getDiaryDetail addEmotionToDiary(postDiaryEmo request) {
        Diary diary = diaryRepository.findById(request.getDiaryIdx())
                .orElseThrow(() -> new MindFriendBusinessException(DIARY_NOT_FOUND));


        diary.setEmotion(request.getEmotion());
        Diary response = diaryRepository.save(diary);

        if (response == null) {
            throw new MindFriendBusinessException(POST_EMO_FAIL);
        }
        return getDiaryDetail.of(response);
    }

    // 여러 일기 삭제
    @Transactional
    public List<Diary> deleteDiary(Long[] diaryIds) {
        List<Diary> deletedDiarys = new ArrayList<>();

        for(Long diaryIdx : diaryIds) {
            Optional<Diary> diaryOptional = diaryRepository.findById(diaryIdx);

            if (diaryOptional.isPresent()) {
                Diary diary = diaryOptional.get();
                deletedDiarys.add(diary);
                diaryRepository.delete(diary);
            }
        }
        return deletedDiarys;
    }

    public List<getDiaryList> getDiaryForEmo(YearMonth yearMonth, Long emotion) {
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        System.out.println(startDateTime);
        System.out.println(endDateTime);

        List<Diary> diaries = diaryRepository.findByCreatedAtBetweenAndMainEmotion(startDateTime, endDateTime, emotion);
        return getDiaryList.of(diaries);

    }
}
