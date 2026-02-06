package com.ai.app.service.ch1;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class Ch1_ChatClientMemoryService {

    private final ChatClient chatClientMemory;

    // Constructor
    public Ch1_ChatClientMemoryService(ChatClient.Builder chatClientBuilder) {
        ChatMemory chatMemory =MessageWindowChatMemory.builder()
                .maxMessages(3)
                .build();
        this.chatClientMemory = chatClientBuilder
                .defaultAdvisors(
                        //    MessageChatMemoryAdvisor: 메모리에서 대화 내역을 검색하여 메시지 모음으로 프롬프트에 포함합니다.
                        //    PromptChatMemoryAdvisor: 메모리에서 대화 내역을 검색하여 시스템 프롬프트에 일반 텍스트로 추가합니다.
                        //    VectorStoreChatMemoryAdvisor: 벡터 저장소에서 대화 내역을 검색하여 시스템 메시지에 일반 텍스트로 추가합니다.
                        //MessageChatMemoryAdvisor.builder(chatMemory).build())
                        PromptChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public Flux<String> chatMemory(String question, String conversationId) {
        return chatClientMemory.prompt()
                .user(question)
                .advisors(advisorSpec -> advisorSpec.param(
                        ChatMemory.CONVERSATION_ID, conversationId
                ))
                .stream()
                .content();
    }
}
