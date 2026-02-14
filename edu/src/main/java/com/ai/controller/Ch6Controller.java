package com.ai.controller;

import com.ai.app.service.ch6.Ch6_AccessSystemToolsService;
import com.ai.app.service.ch6.Ch6_SearchCustomerToolsService;
import com.ai.app.service.ch6.Ch6_ShoppingToolsService;
import com.ai.app.service.ch6.Ch6_TimeWeatherToolsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "6. Tool Calling_6", description = "Tool Calling Controller")
@RestController
@RequestMapping("/ch6")
@Slf4j
@RequiredArgsConstructor
// Chapter 6. Tool Calling Controller
public class Ch6Controller {

    // 1. Date Time
    final Ch6_TimeWeatherToolsService ch6ChatClientService;
    // 2. Customer Inquiry - JSON
    // 2. Customer Inquiry - String
    final Ch6_SearchCustomerToolsService ch6SearchCustomerToolsService;
    // 3. Recommendation
    final Ch6_ShoppingToolsService ch6ShoppingToolsService;
    // 4. Access System
    final Ch6_AccessSystemToolsService ch6OpenAiImageService;

    // 1. Date Time
    @Operation(
            summary = "1. Text to Speech",
            description = """
                ### 1. Text to Speech
            """,
            method = "POST"
    )
    @PostMapping("/data-time")
    public String chatTimeWeather(@RequestParam("prompt") String userPrompt) {
        log.info(userPrompt);
        return ch6ChatClientService.chat1(userPrompt);
    }

    // 2. Customer Inquiry - JSON
    @Operation(
            summary = "2. Customer Inquiry - JSON",
            description = """
                ### 2. Customer Inquiry - JSON
            """,
            method = "POST"
    )
    @PostMapping("/customer-inquiry-json")
    public String getCustomer(@RequestParam("prompt") String userPrompt) {
        log.info(userPrompt);
        return ch6SearchCustomerToolsService.getCustomer(userPrompt);
    }

    // 2. Customer Inquiry - String
    @Operation(
            summary = "2. Customer Inquiry - String",
            description = """
                ### 2. Customer Inquiry - String
            """,
            method = "POST"
    )
    @PostMapping("/customer-inquiry-string")
    public String getCustomerString(@RequestParam("prompt") String userPrompt) {
        log.info(userPrompt);
        return ch6SearchCustomerToolsService.getCustomerString(userPrompt);
    }

    // 3. Recommendation
    @Operation(
            summary = "3. Recommendation",
            description = """
                ### 3. Recommendation
            """,
            method = "POST"
    )
    @PostMapping("/recommendation")
    public String getOrderedByCustomer(@RequestParam("prompt") String userPrompt, @RequestParam("user_id") String userId) {
        log.info(userPrompt);
        return ch6ShoppingToolsService.getPropensity(userPrompt, userId);
    }

    // 4. Access System
    @Operation(
            summary = "4. Access System",
            description = """
                ### 4. Access System
            """,
            method = "POST"
    )
    @PostMapping(value = "/access-system")
    public String accessSystem(
            @RequestParam(value="attach", required = false) MultipartFile attach) throws IOException {
        return ch6OpenAiImageService.imageAnalysisText(attach.getContentType(), attach.getBytes());
    }
}
