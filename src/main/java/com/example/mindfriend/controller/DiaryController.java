package com.example.mindfriend.controller;

import com.example.mindfriend.common.response.result.ResultCode;
import com.example.mindfriend.common.response.result.ResultResponse;
import com.example.mindfriend.domain.Diary;
import com.example.mindfriend.dto.request.postDiary;
import com.example.mindfriend.dto.request.postDiaryEmo;
import com.example.mindfriend.dto.response.getDiary;
import com.example.mindfriend.dto.response.getDiaryDetail;
import com.example.mindfriend.dto.response.getDiaryList;
import com.example.mindfriend.security.SecurityUtils;
import com.example.mindfriend.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResultResponse<getDiary> postDiary(@RequestPart(value = "postDiary") postDiary request, @RequestPart(value = "postImg")MultipartFile postImg) throws IOException {
        getDiary response = diaryService.postDiary(securityUtils.getCurrentUserId(), request, postImg);
        return new ResultResponse<>(ResultCode.POST_DIARY_SUCCESS, response);
    }

    // 다이어리 단건 조회
    @GetMapping("/read")
    public ResultResponse<getDiaryDetail> getDiaryDetail(@RequestBody getDiary request) {
        getDiaryDetail response = diaryService.getDiaryDetail(securityUtils.getCurrentUserId(),request);
        return new ResultResponse<>(GET_DIARY_SUCCESS, response);
    }

    // 다이어리 감정 추가
    @PostMapping("/emo")
    public  ResultResponse<getDiaryDetail> addEmotionToDiary(@RequestBody postDiaryEmo request) {
        getDiaryDetail response =  diaryService.addEmotionToDiary(request);
        return  new ResultResponse<>(GET_DIARY_SUCCESS, response);
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
}
