package com.example.mindfriend.controller;

import com.example.mindfriend.common.response.result.ResultCode;
import com.example.mindfriend.common.response.result.ResultResponse;
import com.example.mindfriend.dto.request.postDiary;
import com.example.mindfriend.dto.request.postDiaryEmo;
import com.example.mindfriend.dto.response.getDiary;
import com.example.mindfriend.dto.response.getDiaryDetail;
import com.example.mindfriend.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.example.mindfriend.common.response.result.ResultCode.GET_DIARY_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
@Slf4j
public class DiaryController {
    private final DiaryService diaryService;

    // 일기 작성
    @PostMapping("/{userId}")
    public ResultResponse<getDiary> postDiary(@PathVariable long userId, @RequestBody postDiary request) {
        getDiary response = diaryService.postDiary(userId, request);
        return new ResultResponse<>(ResultCode.POST_DIARY_SUCCESS, response);
    }

    // 다이어리 단건 조회
    @GetMapping("/{diaryId}")
    public ResultResponse<getDiaryDetail> getDiaryDetail(@PathVariable long diaryId) {
        getDiaryDetail response = diaryService.getDiaryDetail(diaryId);
        return new ResultResponse<>(GET_DIARY_SUCCESS, response);
    }

    // 다이어리 감정 추가
    @PostMapping("/emo")
    public  ResultResponse<getDiaryDetail> addEmotionToDiary(@RequestBody postDiaryEmo request) {
        getDiaryDetail response =  diaryService.addEmotionToDiary(request);
        return  new ResultResponse<>(GET_DIARY_SUCCESS, response);
    }
}
