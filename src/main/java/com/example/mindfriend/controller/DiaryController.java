package com.example.mindfriend.controller;

import com.example.mindfriend.common.response.result.ResultCode;
import com.example.mindfriend.common.response.result.ResultResponse;
import com.example.mindfriend.dto.request.postDiary;
import com.example.mindfriend.dto.response.getDiary;
import com.example.mindfriend.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
}
