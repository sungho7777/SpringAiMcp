package com.ai.app.service.ch1;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
@Slf4j
public class ChatService {

    private final ChatModel chatModel;

    // Constructor
    public ChatService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String requestText(String question){
        SystemMessage systemMessage = SystemMessage.builder()
                .text("질문에 대한 답변을 한국어로 친절하게 답변해야 합니다.").build();
        UserMessage userMessage = UserMessage.builder().text(question).build();
        ChatOptions chatOptions = ChatOptions.builder().build();

        ChatResponse chatResponse = chatModel.call(
                Prompt.builder()
                        .messages(systemMessage, userMessage)
                        .chatOptions(chatOptions).build()
        );
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        return assistantMessage.getText();
    }

    public Flux<String> requestTextStream(String question){
        SystemMessage systemMessage = SystemMessage.builder()
                .text("질문에 대한 답변을 한국어로 친절하게 답변해야 합니다.").build();
        UserMessage userMessage = UserMessage.builder().text(question).build();
        ChatOptions chatOptions = ChatOptions.builder().build();

        Flux<ChatResponse> fluxResponse = chatModel.stream(
                Prompt.builder()
                        .messages(systemMessage, userMessage)
                        .chatOptions(chatOptions).build()
        );
        return fluxResponse.map(chatResponse -> {
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            String chunk = assistantMessage.getText();
            if (chunk == null) chunk = "";
            return chunk;
        });
    }
}
