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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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
    private final S3Uploader s3Uploader;

    // 일기 작성
    @Transactional
    public getDiary postDiary(String userId, postDiary request, MultipartFile postImg) throws IOException {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        final String userPostImg = s3Uploader.upload(postImg, "images/posts/");

        Diary diary = request.toEntity(userPostImg);
        diary.setUser(user);

        Diary response = diaryRepository.save(diary);
        if (response == null) {
            throw new MindFriendBusinessException(POST_DIARY_FAIL);
        }
        return getDiary.of(response);
    }

    // 일기 단건 조회
    public getDiaryDetail getDiaryDetail(String userId, String dateString) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        // Convert Date to LocalDate
        LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);

        LocalDateTime startDateTime = localDate.atStartOfDay();
        LocalDateTime endDateTime = localDate.atTime(LocalTime.MAX);

        List<Diary> diaryList = diaryRepository.findByUserAndCreatedAtBetween(user, startDateTime, endDateTime);

        if (diaryList.isEmpty()) {
            throw new MindFriendBusinessException(DIARY_NOT_FOUND);
        }

        // 여기서 diaryList에서 원하는 일기를 선택하는 로직 추가
        // 예: 첫 번째 일기를 선택한다고 가정
        Diary diary = diaryList.get(0);

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

        List<Diary> diaries = diaryRepository.findByCreatedAtBetweenAndMainEmotion(startDateTime, endDateTime, emotion);

        return getDiaryList.of(diaries);
    }

    public List<getDiaryList> getDiaryForKeyword(YearMonth yearMonth, String keyword) {
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Diary> diaries = diaryRepository.findByCreatedAtBetweenAndContentContaining(startDateTime, endDateTime, keyword);

        return getDiaryList.of(diaries);
    }

    public List<getDiaryList> getDiaryForDate(YearMonth yearMonth) {
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Diary> diaries = diaryRepository.findByCreatedAtBetween(startDateTime, endDateTime);

        return getDiaryList.of(diaries);
    }
}
