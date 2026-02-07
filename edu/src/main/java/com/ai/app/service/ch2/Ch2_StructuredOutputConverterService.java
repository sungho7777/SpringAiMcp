package com.ai.app.service.ch2;

import com.ai.app.dto.ch2.Contents;
import com.ai.app.dto.ch2.Question;
import com.ai.app.dto.ch2.Shop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class Ch2_StructuredOutputConverterService {

    private final ChatClient chatClient;

    // src/main/resources 폴더에 prompts 폴더 생성 후 아래 prompt template 파일을 생성
    @Value("classpath:prompts/system-message-prompt-template.st")
    private Resource systemResource;
    @Value("classpath:prompts/user-message-structured-output.st")
    private Resource userResource;
    @Value("classpath:prompts/user-message-structured-output-map-output.st")
    private Resource userResourceMapOutput;
    @Value("classpath:prompts/user-message-structured-list-output.st")
    private Resource userResourceListOutput;

    // Constructor
    public Ch2_StructuredOutputConverterService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public Contents beanOutputConverter(Question question) {
        SystemPromptTemplate systemTemplate = new SystemPromptTemplate(systemResource);
        SystemPromptTemplate userQuestionTemplate = new SystemPromptTemplate(userResource);
        String userQuestion  = userQuestionTemplate.render(Map.of("location", question.location(), "content", question.content()));
        String systemMessage  = systemTemplate.render(Map.of("language", question.language()));

        return chatClient.prompt()
                .system(systemMessage)
                .user(userQuestion)
                .call()
                .entity(Contents.class);
    }
    public  List<String> listOutputConverter(Question question) {
        SystemPromptTemplate systemTemplate = new SystemPromptTemplate(systemResource);
        SystemPromptTemplate userQuestionTemplate = new SystemPromptTemplate(userResourceListOutput);
        String userQuestion  = userQuestionTemplate.render(Map.of("location", question.location(), "content", question.content()));
        String systemMessage  = systemTemplate.render(Map.of("language", question.language()));

        //userQuestion += "검색 결과는 상점 이름만 조회해줘";

        return chatClient.prompt()
                .system(systemMessage)
                .user(userQuestion)
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));
    }

    public  List<Shop> parameterizedTypeReference(Question question) {
        SystemPromptTemplate systemTemplate = new SystemPromptTemplate(systemResource);
        SystemPromptTemplate userQuestionTemplate = new SystemPromptTemplate(userResource);
        String userQuestion  = userQuestionTemplate.render(Map.of("location", question.location(), "content", question.content()));
        String systemMessage  = systemTemplate.render(Map.of("language", question.language()));

        return chatClient.prompt()
                .system(systemMessage)
                .user(userQuestion)
                .call()
                .entity(new ParameterizedTypeReference<List<Shop>>() {});
    }

    public   Map<String, Object> mapOutputConverter(Question question) {
        SystemPromptTemplate systemTemplate = new SystemPromptTemplate(systemResource);

        SystemPromptTemplate userQuestionTemplate = SystemPromptTemplate.builder()
                .resource(userResourceMapOutput)
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .build();
        String userQuestion  = userQuestionTemplate.render(Map.of("location", question.location(), "content", question.content()));
        String systemMessage  = systemTemplate.render(Map.of("language", question.language()));

        return chatClient.prompt()
                .system(systemMessage)
                .user(userQuestion)
                .call()
                .entity(new MapOutputConverter());
    }

}
