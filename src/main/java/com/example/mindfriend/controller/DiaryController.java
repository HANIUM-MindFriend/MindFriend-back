package com.example.mindfriend.controller;

import com.example.mindfriend.common.response.result.ResultCode;
import com.example.mindfriend.common.response.result.ResultResponse;
import com.example.mindfriend.domain.Diary;
import com.example.mindfriend.dto.request.PredictionRequest;
import com.example.mindfriend.dto.request.postAiDiary;
import com.example.mindfriend.dto.request.postDiary;
import com.example.mindfriend.dto.request.postDiaryEmo;
import com.example.mindfriend.dto.response.*;
import com.example.mindfriend.dto.response.DashBoard.GetDashboard;
import com.example.mindfriend.dto.response.FeedByDay.GetFeedByDay;
import com.example.mindfriend.security.SecurityUtils;
import com.example.mindfriend.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.YearMonth;
import java.util.List;

import static com.example.mindfriend.common.response.result.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
@Slf4j
public class DiaryController {
    private final DiaryService diaryService;
    private final SecurityUtils securityUtils;

    // 일기 작성
    @PostMapping("/write")
    public ResultResponse<getDiary> postDiary(@RequestPart(value = "postDiary") postDiary request, @RequestPart(value = "postImg") MultipartFile postImg) throws IOException, InterruptedException {
        getDiary response = diaryService.postDiary(securityUtils.getCurrentUserId(), request, postImg);
        return new ResultResponse<>(ResultCode.POST_DIARY_SUCCESS, response);
    }

    @PostMapping("/predict")
    public String predict(@RequestBody PredictionRequest request) {
        String sentence = request.getSentence();
        String pythonInterpreterPath = "/Library/Frameworks/Python.framework/Versions/3.7/bin/python3"; // Python 인터프리터 경로 설정
        String pythonScriptPath = "/Users/songjuhee/Desktop/Model/main.py"; // Python 스크립트 경로 설정

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(pythonInterpreterPath, pythonScriptPath, sentence);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            System.out.println("파이참 실행 성공");

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String output;
            StringBuilder result = new StringBuilder();
            while ((output = reader.readLine()) != null) {
                result.append(output).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return result.toString();
            } else {
                return "오류가 발생했습니다";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "오류가 발생했습니다";
        }
    }

    // 다이어리 단건 조회
    @GetMapping("/read")
    public ResultResponse<getDiaryDetail> getDiaryDetail(@RequestParam String date) {
        getDiaryDetail response = diaryService.getDiaryDetail(securityUtils.getCurrentUserId(), date);
        return new ResultResponse<>(GET_DIARY_SUCCESS, response);
    }

    // 다이어리 감정 추가
    @PostMapping("/emo")
    public ResultResponse<getDiaryDetail> addEmotionToDiary(@RequestBody postDiaryEmo request) {
        getDiaryDetail response = diaryService.addEmotionToDiary(request);
        return new ResultResponse<>(GET_DIARY_SUCCESS, response);
    }

    // 일기 삭제
    @DeleteMapping("/deleteList")
    public ResultResponse<List<Diary>> deleteDiary(@RequestBody Long[] diaryIds) {
        List<Diary> response = diaryService.deleteDiary(diaryIds);
        return new ResultResponse<>(DELETE_DIARY_SUCCESS, response);
    }

    // 감정 별 일기 검색(조회)
    @GetMapping("/emotion/{emotion}")
    public ResultResponse<List<getDiaryList>> angryDiary(@PathVariable Long emotion, @RequestParam String yearMonth) {
        YearMonth parsedYearMonth = YearMonth.parse(yearMonth);
        List<getDiaryList> response = diaryService.getDiaryForEmo(parsedYearMonth, emotion);
        return new ResultResponse<>(GET_DIARYLIST_SUCESS, response);
    }

    // 키워드 별 일기 검색(조회)
    @GetMapping("/keyword/search")
    public ResultResponse<List<getDiaryList>> searchForKeyword(@RequestParam String keyword, @RequestParam String yearMonth) {
        YearMonth parsedYearMonth = YearMonth.parse(yearMonth);
        List<getDiaryList> response = diaryService.getDiaryForKeyword(parsedYearMonth, keyword);
        return new ResultResponse<>(GET_DIARYLIST_SUCESS, response);
    }

    // 날짜 별 일기 검색(조회)
    @GetMapping("/date/search")
    public ResultResponse<List<getDiaryList>> searchForDate(@RequestParam String yearMonth) {
        YearMonth parsedYearMonth = YearMonth.parse(yearMonth);
        List<getDiaryList> response = diaryService.getDiaryForDate(parsedYearMonth);
        return new ResultResponse<>(GET_DIARYLIST_SUCESS, response);
    }

    // 일기 작성 후 해당 텍스트 감정 반환
    @PostMapping("/write/emo")
    public ResultResponse<GetContentEmo> postAiDiary(@RequestPart(value = "postAiDiary") postAiDiary request) {
        GetContentEmo response = diaryService.postAiDiary(securityUtils.getCurrentUserId(), request);
        return new ResultResponse<>(POST_DIARY_SUCCESS, response);
    }

    // 다이어리 메인 감정 반환
    @GetMapping("/main/{diaryIdx}")
    public ResultResponse<GetMainEmo> getMainEmotion(@PathVariable Long diaryIdx) {
        GetMainEmo response = diaryService.getMainEmotion(diaryIdx);
        return new ResultResponse<>(GET_MAIN_EMOTION_SUCCEESS, response);
    }

    // chat gpt api
    @PostMapping("/gpt/{diaryIdx}")
    public ResultResponse<GetGptRes> gptChatbot(@PathVariable Long diaryIdx, @RequestBody postDiary request) {
        GetGptRes response = diaryService.getChatbot(diaryIdx, request);
        return new ResultResponse<>(GET_GPT_SUCCESS, response);
    }

    // 개인일기 관리 대시보드 조회
    @GetMapping("/dash")
    public ResultResponse<GetDashboard> getDashBoard(@RequestParam String yearMonth) {
        YearMonth parsedYearMonth = YearMonth.parse(yearMonth);
        GetDashboard response = diaryService.getDashboard(SecurityUtils.getCurrentUserId(), parsedYearMonth);
        return new ResultResponse<>(GET_DASHBOARD_SUCCESS, response);
    }

    // 일 별 피드 조회
    @GetMapping("/feed/{diaryIdx}")
    public ResultResponse<GetFeedByDay> getFeedByDay(@PathVariable Long diaryIdx) {
        GetFeedByDay response = diaryService.getFeed(SecurityUtils.getCurrentUserId(), diaryIdx);
        return new ResultResponse<>(GET_FEED_SUCCESS, response);
    }
}
