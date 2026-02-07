package com.ai.app.service.ch2;

import com.ai.app.dto.ch2.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class Ch2_PromptTemplateService {

    private final ChatClient chatClient;

    private final String userQuestionTemplateText1 = """
                            {location} 지역에 {content} 정보를 5개이상 알려주세요
                            검색 후 시스템에 설정 된 언어로 변역된 내용만 출력 해줘
                        """;
    private final String userQuestionTemplateText2 = """
                            <location> 지역에 <content> 정보를 5개이상 알려주세요
                            검색 후 시스템에 설정 된 언어로 변역된 내용만 출력 해줘
                        """;
    private final String systemTemplateText1 = """
                            사용자의 검색 결과를 {language}로 변역해주세요
                        """;
    private final String systemTemplateText2 = """
                            사용자의 검색 결과를 <language>로 변역해주세요
                        """;


    private final PromptTemplate userQuestionTemplate1 = PromptTemplate.builder()
                                        .template(userQuestionTemplateText1)
                                        .build();
    private final PromptTemplate userQuestionTemplate2 = PromptTemplate.builder()
                                        .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                                        .template(userQuestionTemplateText2)
                                        .build();
    private final PromptTemplate systemTemplate1 = PromptTemplate.builder()
                                        .template(systemTemplateText1)
                                        .build();
    private final PromptTemplate systemTemplate2 = PromptTemplate.builder()
                                        .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                                        .template(systemTemplateText2)
                                        .build();
    // Constructor
    public Ch2_PromptTemplateService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String promptTemplate1(Question question) {
        Prompt prompt = userQuestionTemplate1.create(Map.of("location", question.location(), "content", question.content()));

        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }

    public String promptTemplate2(Question question) {
        String userQuestion = userQuestionTemplate1.render(Map.of("location", question.location(), "content", question.content()));

        return chatClient.prompt()
                .user(userQuestion)
                .call()
                .content();
    }
    public String promptTemplate3(Question question) {
        String userQuestion  = userQuestionTemplate1.render(Map.of("location", question.location(), "content", question.content()));
        String systemMessage  = systemTemplate1.render(Map.of("language", question.language()));

        return chatClient.prompt()
                .user(userQuestion)
                .system(systemMessage)
                .call()
                .content();
    }
    public String promptTemplate4(Question question) {
        Message userQuestionMessage  = userQuestionTemplate1.createMessage(Map.of("location", question.location(), "content", question.content()));
        Message systemMessage  = systemTemplate1.createMessage(Map.of("language", question.language()));
        Prompt prompt = new Prompt(List.of(userQuestionMessage, systemMessage));

        return chatClient.prompt(prompt)
                .call()
                .content();
    }
    public String promptTemplate5(Question question) {
        Message userQuestionMessage  = userQuestionTemplate1.createMessage(Map.of("location", question.location(), "content", question.content()));
        Message systemMessage  = systemTemplate1.createMessage(Map.of("language", question.language()));

        return chatClient.prompt()
                .messages(userQuestionMessage,systemMessage)
                .call()
                .content();
    }


}
