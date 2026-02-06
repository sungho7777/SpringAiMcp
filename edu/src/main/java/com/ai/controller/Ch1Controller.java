package com.ai.controller;

import com.ai.app.service.ch1.Ch1_ChatClientMemoryService;
import com.ai.app.service.ch1.Ch1_ChatClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Tag(name = "1. Chapter_1", description = "Chat Completion")
@RestController
@RequestMapping("/ch1")
@Slf4j
@RequiredArgsConstructor
// Chapter 1. Chat Completion Controller
public class Ch1Controller {

    // 1. Chat (Zero-Shot Prompting)
    // 2. Chat Stream
    // 3. Chat (Few-Shot Prompting)
    // 4. Chat (Chain-of-Thought Prompting)
    final Ch1_ChatClientService ch1ChatClientService;
    // 5. Chat Memory
    final Ch1_ChatClientMemoryService ch1ChatClientMemoryService;

    // 1. Chat (Zero-Shot Prompting)
    @Operation(
            summary = "1. Chat (Zero-Shot Prompting)",
            description = "1. Chat (Zero-Shot Prompting)",
            method = "POST"
    )
    @PostMapping("/completion")
    public String chatCompletion(@RequestParam("prompt") String userPrompt) {
        log.info(userPrompt);
        return ch1ChatClientService.chat(userPrompt);
    }

    // 2. Chat Stream
    @Operation(
            summary = "2. Chat Stream",
            description = "2. Chat Stream",
            method = "POST"
    )
    @PostMapping("/stream")
    public Flux<String> chatStream(@RequestParam("prompt") String userPrompt) {
        log.info(userPrompt);
        return ch1ChatClientService.chatStream(userPrompt);
    }

    // 3. Chat (Few-Shot Prompting)
    @Operation(
            summary = "3. Chat (Few-Shot Prompting)",
            description = "3. Chat (Few-Shot Prompting)",
            method = "POST"
    )
    @PostMapping("/few")
    public String chatFewShot(@RequestParam("prompt") String userPrompt) {
        log.info(userPrompt);
        return ch1ChatClientService.chatFewShot(userPrompt);
    }

    // 4. Chat (Chain-of-Thought Prompting)
    @Operation(
            summary = "4. Chat (Chain-of-Thought Prompting)",
            description = "4. Chat (Chain-of-Thought Prompting)",
            method = "POST"
    )
    @PostMapping("/chain")
    public Flux<String> chatChainOfThought(@RequestParam("prompt") String userPrompt) {
        log.info(userPrompt);
        return ch1ChatClientService.chatChainOfThought(userPrompt);
    }

    // 5. Chat Memory
    @Operation(
            summary = "5. Chat Memory",
            description = "5. Chat Memory",
            method = "POST"
    )
    @PostMapping("/memory")
    public Flux<String> chatMemory(@RequestParam("prompt") String userPrompt, HttpSession session) {
        log.info(userPrompt);
        return ch1ChatClientMemoryService.chatMemory(userPrompt, session.getId());
    }
}
