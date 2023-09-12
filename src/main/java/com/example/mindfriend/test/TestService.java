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
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            // 스크립트 실행 후 결과를 읽어옴.
            StringBuilder output = new StringBuilder();
            try (InputStream inputStream = process.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            modelExecutionResult = output.toString().trim();
            System.out.println(modelExecutionResult);
            log.info(modelExecutionResult);

            return modelExecutionResult;
        } catch (IOException | InterruptedException e) {
            // Handle exceptions here
            e.printStackTrace();
        }
        log.info(modelExecutionResult);
        return modelExecutionResult;
    }
}
