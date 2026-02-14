package com.ai.app.service.ch6;

import com.ai.app.service.ch6.tools.card.AccessSystemTools;
import com.ai.app.service.ch6.tools.card.IdCardTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

@Service
@Slf4j
public class Ch6_AccessSystemToolsService {
    private final ChatClient chatClient;

    // 시스템 메시지 생성
    String systemMessageText ="""
          너는 이미지 분석가 입니다.
 
        """;
    String userMessageText ="""
          사용자가 전송한 이미지를 기반으로 사용자의 질문에 맞게 분석하고 답변을 한국어로 하세요. 
          답변을 만들때는 숫자로만 알려줘
          숫자가 직원의 사번으로 사용되며 모든 직원의 사번과 일치하는지 검사 한다.
          사번이 일치하면 출입문을 연다.
          사번이 일치하지 않으면 출문을 열수 없다.
        """;

    // Constructor
    public Ch6_AccessSystemToolsService(ChatClient.Builder chatClientBuilder, ImageModel imageModel) {
        chatClient = chatClientBuilder.build();
    }

    public String imageAnalysisText(String contentType, byte[] bytes) {


        Media media = Media.builder()
                .mimeType(MimeType.valueOf(contentType))
                .data(new ByteArrayResource(bytes))
                .build();
        UserMessage userMessage = UserMessage.builder()
                .text(userMessageText)
                .media(media)
                .build();

        return chatClient.prompt()
                .system(systemMessageText)
                .messages(userMessage)
                .tools(new AccessSystemTools(), new IdCardTools())
                .call()
                .content();
    }

}
