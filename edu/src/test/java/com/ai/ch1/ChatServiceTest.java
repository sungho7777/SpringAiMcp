package com.ai.ch1;

import com.ai.app.service.ch1.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
@Slf4j
class ChatServiceTest {

    @Autowired
    ChatService chatService;

    @Test
    void contextLoads() {
        String question = "인천 부평에 있는 맛집 알려줘.";
        String result = chatService.requestText(question);
        log.info("result:{}", result);
    }

    @Test
    void contextLoadsFlux() {
        String question = "인천 부평에 있는 맛집 알려줘.";
        Flux<String> result = chatService.requestTextStream(question);
        result
                .collectList()
                .block()
                .stream()
                .forEach(System.out::println);
    }

}
