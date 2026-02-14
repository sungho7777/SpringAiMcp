package com.ai.controller;

import com.ai.app.service.ch6.Ch6_TimeWeatherToolsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ch6")
@Slf4j
@RequiredArgsConstructor
// Chapter 6. Tool Calling Controller
public class Ch6Controller {

    // 1. Date Time
    final Ch6_TimeWeatherToolsService ch6ChatClientService;

    // 1. Date Time
    @RequestMapping("/data-time")
    public String chatTimeWeather(@RequestParam("prompt") String userPrompt) {
        log.info(userPrompt);
        return ch6ChatClientService.chat1(userPrompt);
    }

}
