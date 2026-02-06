package com.ai.ch1;


import com.ai.app.service.ch1.Ch1_ChatClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ChatClientServiceTestByMock {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    ChatClient chatClient;

    @Mock
    ChatClient.Builder chatClientBuilder;

    Ch1_ChatClientService service;

    @BeforeEach
    void setUp() {
        // 🔑 반드시 먼저 stub
        when(chatClientBuilder.build()).thenReturn(chatClient);

        // 🔑 그리고 직접 생성
        service = new Ch1_ChatClientService(chatClientBuilder);
    }

    @Test
    void chatFewShot() {

        String json = """
                [
                  {
                    "name": "테스트 맛집",
                    "menu": ["김치찌개", "된장찌개"],
                    "address": "서울 종로구"
                  }
                ]
                """;

        when(chatClient.prompt()
                .user(anyString())
                .call()
                .content())
                .thenReturn(json);

        String result = service.chatFewShot("서울 종로 맛집 알려줘");

        assertThat(result).contains("테스트 맛집");
    }

    @Test
    void chat() {

        when(chatClient.prompt()
                .system(anyString())
                .user(anyString())
                .call()
                .content())
                .thenReturn("테스트 응답입니다");

        String result = service.chat("안녕");

        assertThat(result).isEqualTo("테스트 응답입니다");
    }

    @Test
    void chatStream() {

        when(chatClient.prompt()
                .system(anyString())
                .user(anyString())
                .stream()
                .content())
                .thenReturn(Flux.just("안녕", "하세요"));

        StepVerifier.create(service.chatStream("인사"))
                .expectNext("안녕")
                .expectNext("하세요")
                .verifyComplete();
    }
}

