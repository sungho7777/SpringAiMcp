package com.ai.controller;

import com.ai.app.service.ch4.Ch4_OpenAiImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Objects;

@Tag(name = "4. Chapter_4", description = "Multimodality API – Images & Vision Controller")
@RestController
@RequestMapping("/ch4")
@Slf4j
@RequiredArgsConstructor
// Chapter 4. Multimodality API – Images & Vision Controller
public class Ch4Controller {

    final Ch4_OpenAiImageService ch4OpenAiImageService;

    // 1. Generate Image for URL
    @Operation(
            summary = "1. Generate Image for URL",
            description = """
                ### 1. Generate Image for URL
                ### @("/generate-image-url")
                POST http://localhost:8080/ch4/generate-image-url
                Content-Type: application/x-www-form-urlencoded
                Accept: application/x-ndjson;charset=UTF-8
                
                prompt=2050년 유행 할거 같은 개인 비행기 디자인으로 애니메이션화 해서 이미지 생성해줘.
            """,
            method = "POST"
    )
    @PostMapping("/generate-image-url")
    public String generateImageUrl(@RequestParam("prompt") String question) {
        log.info(question);
        return ch4OpenAiImageService.generateImageUrl(question);
    }

    // 2. Generate Image
    @Operation(
            summary = "2. Generate Image",
            description = """
                ### 2. Generate Image
                ### @("/generate-image")
                POST http://localhost:8080/ch4/generate-image
                Content-Type: application/x-www-form-urlencoded
                Accept: application/x-ndjson;charset=UTF-8
                
                prompt=야경이 멋진 인천 앞바다의 모습
            """,
            method = "POST"
    )
    @PostMapping("/generate-image")
    public String generateImage(@RequestParam("prompt") String question) {
        log.info(question);
        return ch4OpenAiImageService.generateImageToText(question);
    }

    // 3. Image Analysis
    @Operation(
            summary = "3. Image Analysis",
            description = """
                ### 3. Image Analysis
                ### @("/image-analysis")
                POST http://localhost:8080/ch4/image-analysis
                Content-Type: application/x-www-form-urlencoded
                Accept: application/x-ndjson;charset=UTF-8
                
                question=
                attach=
            """,
            method = "POST"
    )
    @PostMapping(value = "/image-analysis")
    public Flux<String> imageAnalysis(
            @RequestParam("question") String question,
            @RequestParam(value="attach", required = false) MultipartFile attach) throws IOException {
        // 이미지가 업로드 되지 않았을 경우
        if (attach == null || !Objects.requireNonNull(attach.getContentType()).contains("image/")) {
            return Flux.just("이미지를 올려주세요.");
        }
        return ch4OpenAiImageService.imageAnalysis(question, attach.getContentType(), attach.getBytes());
    }
}
