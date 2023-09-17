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

        String content = request.getContent();

        String command = "sh /home/ubuntu/app/deploy/src/main/java/com/example/mindfriend/script/run_python_script.sh " + content;
        String modelExecutionResult = null;

        try {
            log.info("명령어 실행 및 결과 처리");
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            log.info("실행한 명령어의 종료 " + exitCode);

            // 스크립트 실행 후 결과를 읽어옴.
            log.info("스크립트 실행");
            StringBuilder output = new StringBuilder();
            log.info("결과를 저장할 객체 output" + output);

            try (InputStream inputStream = process.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                log.info("프로세스의 출력 스트림을 열고 이를 읽어오는 BufferedReader");
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("line: " + line);
                    output.append(line).append("\n");
                    log.info("출력 스트임에서 한 줄씩 읽어와 output 변수에 추가");
                }
            }

            modelExecutionResult = output.toString().trim();
            log.info("결과를 콘솔에 출력하고 로깅 " + modelExecutionResult);
            return modelExecutionResult;

        } catch (IOException | InterruptedException e) {
            // Handle exceptions here
            e.printStackTrace();
            e.getMessage();
        }
        log.info(modelExecutionResult);
        return modelExecutionResult;
    }
}
