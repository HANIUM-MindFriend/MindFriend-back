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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private final S3Uploader s3Uploader;

    // 일기 작성
    @Transactional
    public getDiary postDiary(String userId, postDiary request, MultipartFile postImg) throws IOException, InterruptedException {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        final String userPostImg = s3Uploader.upload(postImg, "images/posts/");

        String content = request.getContent(); // 요청에서 content 값을 가져옴

        // 모델 실행에 content 값을 전달하고 결과를 얻어오는 작업 수행
        String modelResult = runModelWithContent(content);

        String command = "sh /Users/songjuhee/Desktop/MindFriend-back/src/main/java/com/example/mindfriend/script/run_python_script.sh";
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            // Processing to read the result after executing the script
            StringBuilder output = new StringBuilder();
            try (InputStream inputStream = process.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            String modelExecutionResult = output.toString().trim();
            System.out.println(modelExecutionResult);
        } catch (IOException | InterruptedException e) {
            // Handle exceptions here
            e.printStackTrace();
        }

        Diary diary = request.toEntity(userPostImg);
        diary.setUser(user);

        Diary response = diaryRepository.save(diary);
        if (response == null) {
            throw new MindFriendBusinessException(POST_DIARY_FAIL);
        }
        return getDiary.of(response);
    }

    private String runModelWithContent(String content) {
        // content 값을 가지고 모델 실행을 수행하고 결과를 반환하는 코드 작성
        // 이 메서드를 실제 모델 실행 코드로 대체해야 합니다.
        return "모델 실행 결과"; // 예시로 임시로 결과를 반환하도록 설정
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
