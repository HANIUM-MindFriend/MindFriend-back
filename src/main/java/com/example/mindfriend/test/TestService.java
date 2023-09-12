package com.example.mindfriend.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TestService {

    @Transactional
    public String predict(testDto request) {
        String content = request.getContent(); // 요청에서 content 값 가져옴

        // 모델 실행에 content 값을 전달하고 결과를 얻어오는 작업 수행
        String modelResult = runModelWithContent(content);

        String command = "sh /home/ubuntu/app/deploy/src/main/java/com/example/mindfriend/script/run_python_script.sh";
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
        return content;
    }

    private String runModelWithContent(String content) {
        // content 값을 가지고 모델 실행을 수행하고 결과를 반환하는 코드 작성
        // 이 메서드를 실제 모델 실행 코드로 대체해야 합니다.
        return "모델 실행 결과"; // 예시로 임시로 결과를 반환하도록 설정
    }
}
