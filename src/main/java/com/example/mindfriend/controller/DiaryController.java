package com.example.mindfriend.controller;

import com.example.mindfriend.common.response.exception.MindFriendBusinessException;
import com.example.mindfriend.common.response.result.ResultCode;
import com.example.mindfriend.common.response.result.ResultResponse;
import com.example.mindfriend.domain.Diary;
import com.example.mindfriend.dto.request.PredictionRequest;
import com.example.mindfriend.dto.request.postAiDiary;
import com.example.mindfriend.dto.request.postDiary;
import com.example.mindfriend.dto.request.postDiaryEmo;
import com.example.mindfriend.dto.response.DashBoard.GetDashboard;
import com.example.mindfriend.dto.response.FeedByDay.GetFeedByDay;
import com.example.mindfriend.dto.response.*;
import com.example.mindfriend.security.SecurityUtils;
import com.example.mindfriend.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import static com.example.mindfriend.common.response.exception.ErrorCode.DIARY_NOT_FOUND;
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
    public ResultResponse<GetDiary> postDiary(@RequestPart(value = "postDiary") postDiary request, @RequestPart(value = "postImg") MultipartFile postImg) throws IOException, InterruptedException {
        GetDiary response = diaryService.postDiary(securityUtils.getCurrentUserId(), request, postImg);
        return new ResultResponse<>(ResultCode.POST_DIARY_SUCCESS, response);
    }

    // 다이어리 단건 조회
    @GetMapping("/read")
    public ResultResponse<GetDiaryDetail> getDiaryDetail(@RequestParam String date) {
        GetDiaryDetail response = diaryService.getDiaryDetail(securityUtils.getCurrentUserId(), date);
        return new ResultResponse<>(GET_DIARY_SUCCESS, response);
    }

    // 다이어리 감정 추가
    @PostMapping("/emo")
    public ResultResponse<GetDiaryDetail> addEmotionToDiary(@RequestBody postDiaryEmo request) {
        GetDiaryDetail response = diaryService.addEmotionToDiary(request);
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
    public ResultResponse<List<GetDiaryList>> angryDiary(@PathVariable Long emotion, @RequestParam String yearMonth) {
        YearMonth parsedYearMonth = YearMonth.parse(yearMonth);
        List<GetDiaryList> response = diaryService.getDiaryForEmo(parsedYearMonth, emotion);
        return new ResultResponse<>(GET_DIARYLIST_SUCESS, response);
    }

    // 키워드 별 일기 검색(조회)
    @GetMapping("/keyword/search")
    public ResultResponse<List<GetDiaryList>> searchForKeyword(@RequestParam String keyword, @RequestParam String yearMonth) {
        YearMonth parsedYearMonth = YearMonth.parse(yearMonth);
        List<GetDiaryList> response = diaryService.getDiaryForKeyword(parsedYearMonth, keyword);
        return new ResultResponse<>(GET_DIARYLIST_SUCESS, response);
    }

    // 날짜 별 일기 검색(조회)
    @GetMapping("/date/search")
    public ResultResponse<List<GetDiaryList>> searchForDate(@RequestParam String yearMonth) {
        YearMonth parsedYearMonth = YearMonth.parse(yearMonth);
        List<GetDiaryList> response = diaryService.getDiaryForDate(parsedYearMonth);
        return new ResultResponse<>(GET_DIARYLIST_SUCESS, response);
    }

    // 일기 작성 후 해당 텍스트 감정 반환
    @PostMapping("/write/emo")
    public ResultResponse<GetContentEmo> postAiDiary(@RequestPart(value = "postAiDiary") postAiDiary request) {
        GetContentEmo response = diaryService.postAiDiary(securityUtils.getCurrentUserId(), request);
        return new ResultResponse<>(POST_DIARY_SUCCESS, response);
    }

    // 다이어리 메인 감정 반환
    @GetMapping("/main")
    public ResultResponse<GetMainEmo> getMainEmotion(@RequestParam String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(date); // 문자열을 Date로 파싱

            GetMainEmo response = diaryService.getMainEmotion(securityUtils.getCurrentUserId(), parsedDate);
            return new ResultResponse<>(GET_MAIN_EMOTION_SUCCEESS, response);
        } catch (ParseException e) {
            e.printStackTrace();
            throw  new MindFriendBusinessException(DIARY_NOT_FOUND);
        }
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
