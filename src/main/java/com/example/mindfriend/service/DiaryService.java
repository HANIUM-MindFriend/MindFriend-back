package com.example.mindfriend.service;

import com.example.mindfriend.common.response.exception.MindFriendBusinessException;
import com.example.mindfriend.common.response.exception.UserNotFoundException;
import com.example.mindfriend.domain.Diary;
import com.example.mindfriend.domain.User;
import com.example.mindfriend.dto.request.postAiDiary;
import com.example.mindfriend.dto.request.postDiary;
import com.example.mindfriend.dto.request.postDiaryEmo;
import com.example.mindfriend.dto.response.*;
import com.example.mindfriend.dto.response.DashBoard.GetDashboard;
import com.example.mindfriend.dto.response.FeedByDay.GetFeedByDay;
import com.example.mindfriend.dto.response.FeedByDay.GetMainEmoList;
import com.example.mindfriend.repository.DiaryRepository;
import com.example.mindfriend.repository.UserRepository;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.mindfriend.common.response.exception.ErrorCode.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DiaryService {
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final S3Uploader s3Uploader;
    private final ChatgptService chatgptService;

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
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            String modelExecutionResult = output.toString().trim();
            System.out.println(modelExecutionResult);
        } catch (IOException | InterruptedException e) {
            // Handle exceptions here
            e.printStackTrace();
        }

        // 오늘 날짜 일기 찾기
        LocalDateTime today = LocalDateTime.now();

        Diary existingDiary = diaryRepository.findDiariesCreatedToday(user, today);

        if (existingDiary != null) {

            // 이미 오늘 일기가 존재한다면 내용을 추가
            existingDiary.setTitle(request.getTitle());
            existingDiary.setContent(request.getContent());
            existingDiary.setImage(userPostImg);

            Diary response = diaryRepository.save(existingDiary);
            if (response == null) {
                throw new MindFriendBusinessException(POST_DIARY_FAIL);
            }
            return getDiary.of(response);
        }
        throw new MindFriendBusinessException(POST_DIARY_FAIL);
    }


    private String runModelWithContent(String content) {
        // content 값을 가지고 모델 실행을 수행하고 결과를 반환하는 코드 작성
        // 이 메서드를 실제 모델 실행 코드로 대체해야 합니다.
        return "모델 실행 결과"; // 예시로 임시로 결과를 반환하도록 설정
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

    public GetContentEmo postAiDiary(String userIdx, postAiDiary request) {
        User user = userRepository.findByUserId(userIdx)
                .orElseThrow(UserNotFoundException::new);

        /*
        ai 텍스트 분석이 처리가 완료되면
        해당 텍스트에 대한 감정 인덱스 반환 코드 추가 예정
        현재는 분석이 완료되었다는 가정하에 랜덤값 반환됨
         */

        Random random = new Random();

        int randomInt = random.nextInt(7);

        LocalDateTime today = LocalDateTime.now();
        Diary existingDiary = diaryRepository.findDiariesCreatedToday(user, today);

        String chatbot = chatgptService.sendMessage(request.getContent() + " 대화하는 듯한 어투를 사용해서 공감과 위로의 말을 한 문장으로 해줘. 문장은 20글자 이하여야돼. ");
        log.info("사용자 [" + user.getUserEmail() + "] " + request.getContent());
        log.info("사용자 [" + user.getUserEmail() + "] 챗봇 답변: " + chatbot );

        // 기존에 작성한 일기가 없다면
        if (existingDiary == null) {
            Diary diary = new Diary();
            diary.setUser(user);
            String increasedEmotion = diary.increaseEmo(randomInt);

            Diary response = diaryRepository.save(diary);

            return GetContentEmo.of(response, randomInt, increasedEmotion);
        } else {
            String increasedEmotion = existingDiary.increaseEmo(randomInt);
            Diary updatedDiary = diaryRepository.save(existingDiary);

            diaryRepository.updateMainEmotion(updatedDiary.getDiaryIdx());

            return GetContentEmo.of(updatedDiary, randomInt, increasedEmotion);
        }
    }

    public GetMainEmo getMainEmotion(Long diaryIdx) {
        Optional<Diary> diary = diaryRepository.findById(diaryIdx);

        return GetMainEmo.of(diary);
    }

    public GetGptRes getChatbot(Long diaryIdx, postDiary request) {
        // chatGPT 에게 질문을 던지기
        Diary diary = diaryRepository.getReferenceById(diaryIdx);
        String response = chatgptService.sendMessage(request.getContent() + " 대화하는 듯한 어투를 사용해서 공감과 위로의 말을 한 문장으로 해줘. 문장은 20글자 이하여야돼. ");
        return GetGptRes.of(diary, response);
    }

    public GetDashboard getDashboard(String userIdx, YearMonth yearMonth) {

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        User user = userRepository.findByUserId(userIdx)
                .orElseThrow(UserNotFoundException::new);

        List<Object[]> emoGraph = diaryRepository.sumEmotionsByUserAndDate(user, startDateTime, endDateTime);

        int[] emotionArray = new int[7];
        for (Object[] row : emoGraph) {
            for (int i = 0; i<row.length; i++) {
                emotionArray[i] += ((Number) row[i]).intValue();
            }
        }
        List<Diary> diary = diaryRepository.findByCreatedAtBetween(startDateTime, endDateTime);

        log.info("사용자 [" + user.getUserEmail() + "] 의 개인일기 관리 대시보드 조회");

        return GetDashboard.of(emotionArray, diary);
    }

    public GetFeedByDay getFeed(String userIdx, Long diaryIdx) {
        User user = userRepository.findByUserId(userIdx)
                .orElseThrow(UserNotFoundException::new);

        List<Diary> mainEmotion = diaryRepository.findAllById(Collections.singleton(diaryIdx));

        List<GetMainEmoList> mainEmoLists = GetMainEmoList.of(mainEmotion);
        Diary diary = diaryRepository.getReferenceById(diaryIdx);

        LocalDateTime createdAt = diary.getCreatedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedDate = createdAt.format(formatter);

        log.info("사용자 [" + user.getUserEmail() + "] 의 피드 조회");
        log.info("사용자 [" + user.getUserEmail() + "] " + formattedDate + "월의 주요 감정은 [" + diary.getMainEmotion() + "]");

        return new GetFeedByDay(diaryIdx, diary.getMainEmotion(), mainEmoLists);
    }
}
