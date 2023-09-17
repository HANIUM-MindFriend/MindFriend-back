package com.example.mindfriend.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Slf4j
public class TestController {
    private final TestService testService;

    @PostMapping("/")
    public ResponseEntity<String> post(@RequestBody testDto request) {
        String result = testService.predict(request);
        return ResponseEntity.ok(result);
    }
}
