package com.ai.controller;

import com.ai.app.dto.ch2.Contents;
import com.ai.app.dto.ch2.Question;
import com.ai.app.dto.ch2.Shop;
import com.ai.app.service.ch2.Ch2_PromptTemplateResourceService;
import com.ai.app.service.ch2.Ch2_StructuredOutputConverterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "2. Chapter_2", description = "Prompt Template Controller")
@RestController
@RequestMapping("/ch2")
@Slf4j
@RequiredArgsConstructor
// Chapter 2. Prompt Template Controller
public class Ch2Controller {

    // 1. Prompt Template
    final Ch2_PromptTemplateResourceService ch2PromptTemplateResourceService;
    // 2. ListOutputConverter
    // 3. MapOutputConverter
    // 4. BeanOutputConverter
    // 5. ParameterizedType
    final Ch2_StructuredOutputConverterService ch2StructuredOutputConverterService;

    // 1. Prompt Template
    @Operation(
            summary = "1. Prompt Template",
            description = """
                ### 1. Prompt Template
                ### @("/prompt-template")
                POST http://localhost:8080/ch2/prompt-template
                Content-Type: application/x-www-form-urlencoded
                Accept: text/plain;charset=UTF-8
                
                location=서울&content=병원&language=Korean
            """,
            method = "POST"
    )
    @PostMapping("/prompt-template")
    public String promtpTemplate(Question question) {
        log.info(question.toString());
        return ch2PromptTemplateResourceService.promptTemplate3(question);
    }

    // 2. ListOutputConverter
    @Operation(
            summary = "2. ListOutputConverter",
            description = "2. ListOutputConverter",
            method = "POST"
    )
    @PostMapping("/list-output")
    public List<String> listOutput(Question question) {
        log.info(question.toString());
        return ch2StructuredOutputConverterService.listOutputConverter(question);
    }

    // 3. MapOutputConverter
    @Operation(
            summary = "3. MapOutputConverter",
            description = "3. MapOutputConverter",
            method = "POST"
    )
    @PostMapping("/map-output")
    public Map<String, Object> mapOutput(Question question) {
        log.info(question.toString());
        return ch2StructuredOutputConverterService.mapOutputConverter(question);
    }

    // 4. BeanOutputConverter
    @Operation(
            summary = "4. BeanOutputConverter",
            description = "4. BeanOutputConverter",
            method = "POST"
    )
    @PostMapping("/bean-output")
    public Contents beanOutput(Question question) {
        log.info(question.toString());
        return ch2StructuredOutputConverterService.beanOutputConverter(question);
    }

    // 5. ParameterizedType
    @Operation(
            summary = "5. ParameterizedType",
            description = "5. ParameterizedType",
            method = "POST"
    )
    @PostMapping("/parameterized-type-reference")
    public List<Shop> parameterizedTypeReference(Question question) {
        log.info(question.toString());
        return ch2StructuredOutputConverterService.parameterizedTypeReference(question);
    }

}
