package com.ai.app.service.ch4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageMessage;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@Slf4j
public class Ch4_OpenAiImageService {

    private final ChatClient chatClient;
    private final ImageModel imageModel;

    // System Message 생성
    String systemMessageText ="""
          너는 이미지 분석가 입니다.
          사용자가 전송한 이미지를 기반으로 사용자의 질문에 맞게 분석하고 답변을 한국어로 하세요.
        """;
    private final PromptTemplate systemPrompt = PromptTemplate.builder()
            .template(systemMessageText)
            .build();

    // Constructor
    public Ch4_OpenAiImageService(ChatClient.Builder chatClientBuilder, ImageModel imageModel) {
        this.imageModel = imageModel;
        chatClient = chatClientBuilder.build();
    }

    // Text를 이미지 URL로 생성
    public String generateImageUrl(String description) {
        return generateImage(description, "url")
                .getResult()
                .getOutput()
                .getUrl();
    }

    // Text를 이미지 파일로 생성
    public String generateImageToText(String description) {
        return generateImage(description, "b64_json")
                .getResult()
                .getOutput()
                .getB64Json();
    }

    // Text를 이미지로 생성 Format에 따라 URL 또는 Image 파일로 생성
    private ImageResponse generateImage(String description, String format) {
        ImageMessage imageMessage = new ImageMessage(description);
        // dall-e-3 model 설정
        OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
                .model("dall-e-3")
                .responseFormat(format)
                .width(1024)
                .height(1024)
                .N(1)
                .build();
        List<ImageMessage> imageMessageList = List.of(imageMessage);
        ImagePrompt imagePrompt = new ImagePrompt(imageMessageList, imageOptions);
        return imageModel.call(imagePrompt);
    }

    // Image 파일과 질문을 이용해 Image 분석
    public Flux<String> imageAnalysis(String question, String contentType, byte[] bytes) {
        Message systemMessage  = systemPrompt.createMessage();

        Media media = Media.builder()
                .mimeType(MimeType.valueOf(contentType))
                .data(new ByteArrayResource(bytes))
                .build();

        UserMessage userMessage = UserMessage.builder()
                .text(question)
                .media(media)
                .build();

        return chatClient.prompt()
                .messages(userMessage,systemMessage)
                .stream()
                .content();
    }

}
