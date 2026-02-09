package com.ai.app.service.ch3;


import com.ai.app.service.ch3.advisor.CheckCharSizeAdvisor;
import com.ai.app.service.ch3.advisor.ReReadingAdvisor;
import com.ai.app.service.ch3.advisor.SimpleLoggerAdvisorHigh;
import com.ai.app.service.ch3.advisor.SimpleLoggerAdvisorLow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@Slf4j
public class Ch3_AdvisorService {

    private final ChatClient chatClient;
    private final ChatClient chatClientMemory;

    // Constructor
    public Ch3_AdvisorService(ChatClient.Builder chatClientBuilder) {
        // Logger Advisor - yml파일에서 반드시 debug로 셋팅 해야 출력 됨
        SimpleLoggerAdvisor customLogger = new SimpleLoggerAdvisor(
                request -> "[SimpleLoggerAdvisor] Custom request: " + request.prompt().getUserMessage(),
                response -> "[SimpleLoggerAdvisor] Custom response: " + response.getResult(),
                Ordered.HIGHEST_PRECEDENCE);
        // 입력되는 내용에 이상 문자 감지 Adviser
        SafeGuardAdvisor safeGuardAdvisor = new SafeGuardAdvisor(
                List.of("스미싱", "무기", "비밀번호"),
                "사용자의 질문에 문제가 있는 단어가 있으면 시스템에 요청 할수 없습니다.",
                Ordered.HIGHEST_PRECEDENCE);

        this.chatClient = chatClientBuilder
                // Add Advisor
                .defaultAdvisors(customLogger, new CheckCharSizeAdvisor(), safeGuardAdvisor)
                .build();

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(3)
                .build();
        this.chatClientMemory = chatClientBuilder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String chat(String question) {
        return chatClient.prompt()
                //.advisors(new SimpleLoggerAdvisorLow(), new SimpleLoggerAdvisorHigh())
                .advisors(new ReReadingAdvisor())
                .system("질문에 대한 답변을 한국어로 친절하게 답변해야 합니다.")
                .user(question)
                .call()
                .content();
    }

    public Flux<String> chatStream(String question) {
        return chatClient.prompt()
                .advisors(new SimpleLoggerAdvisorLow(), new SimpleLoggerAdvisorHigh())
                .system("질문에 대한 답변을 한국어로 친절하게 답변해야 합니다.")
                .user(question)
                .stream()
                .content();
    }

    public Flux<String> chatMemory(String question, String conversationId) {
        return chatClientMemory.prompt()
                .system("질문에 대한 답변을 한국어로 친절하게 답변해야 합니다.")
                .user(question)
                .advisors(advisorSpec -> advisorSpec.param(
                        ChatMemory.CONVERSATION_ID, conversationId
                ))
                .stream()
                .content();
    }

}

