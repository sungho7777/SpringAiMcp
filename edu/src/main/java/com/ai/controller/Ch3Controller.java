package com.ai.controller;

import com.ai.app.dto.ch2.Contents;
import com.ai.app.dto.ch2.Question;
import com.ai.app.service.ch3.Ch3_AdvisorService;
import com.ai.app.service.ch3.Ch3_StructuredOutputConverterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "3. Chapter_3", description = "Advisors Controller")
@RestController
@RequestMapping("/ch3")
@Slf4j
@RequiredArgsConstructor
// Chapter 3. Advisors Controller
public class Ch3Controller {

    // 1. Advisor
    // 2. Advisor: Stream
    final Ch3_AdvisorService ch3AdvisorService;
    // 3. Recursive Advisors
    final Ch3_StructuredOutputConverterService ch3_StructuredOutputConverterService;

    // 1. Advisor
    @Operation(
            summary = "1. completion",
            description = """
                ### 1. Advisor
                ### @("/completion")
                POST http://localhost:8080/ch3/completion
                Content-Type: application/x-www-form-urlencoded
                Accept: text/plain;charset=UTF-8
                
                prompt=테슬라 Y 차량에 대해 장점, 단점 알려줘.
            """,
            method = "POST"
    )
    @PostMapping("/completion")
    public String chatCompletion(@RequestParam("prompt") String userPrompt) {
        log.info(userPrompt);
        return ch3AdvisorService.chat(userPrompt);
    }

    // 2. Advisor: Stream
    @Operation(
            summary = "2. stream",
            description = """
                ### 2. Advisor stream
                ### @("/stream")
                POST http://localhost:8080/ch3/stream
                Content-Type: application/x-www-form-urlencoded
                Accept: text/plain;charset=UTF-8
                
                prompt=미국 ETF 중 나스닥에 해당하는 종목 1개 추천 해주고 이유를 설명 해줘
            """,
            method = "POST"
    )
    @PostMapping("/stream")
    public Flux<String> chatStream(@RequestParam("prompt") String userPrompt) {
        log.info(userPrompt);
        return ch3AdvisorService.chatStream(userPrompt);
    }

    // 3. Recursive Advisors
    @Operation(
            summary = "3. bean-output",
            description = """
                ### 3. Recursive Advisors
                ### @("/bean-output")
                POST http://localhost:8080/ch3/bean-output
                Content-Type: application/x-www-form-urlencoded
                Accept: application/json;charset=UTF-8
                
                location=충남 예산&content=시장(장터)&language=Korean
            """,
            method = "POST"
    )
    @PostMapping("/bean-output")
    public Contents beanOutput(Question question) {
        log.info(question.toString());
        return ch3_StructuredOutputConverterService.beanOutputConverter(question);
    }

}
