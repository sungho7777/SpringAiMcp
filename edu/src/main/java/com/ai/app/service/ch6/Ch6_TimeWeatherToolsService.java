package com.ai.app.service.ch6;

import com.ai.app.service.ch6.tools.time.DateTimeTools;
import com.ai.app.service.ch6.tools.weather.CurrentWeatherTools;
import com.ai.app.service.ch6.tools.weather.ForecastWeatherTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Ch6_TimeWeatherToolsService {

    private final ChatClient chatClient;
    private final ChatOptions chatOptions;

    // Constructor
    public Ch6_TimeWeatherToolsService(ChatClient.Builder chatClientBuilder) {
        ToolCallback[] toolCallbacks = ToolCallbacks.from(new DateTimeTools(), new CurrentWeatherTools(), new ForecastWeatherTools());

        this.chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(toolCallbacks)
                .build();

        this.chatClient = chatClientBuilder
                .defaultOptions(chatOptions)
                .build();
    }

    public String chat1(String question) {
        return chatClient.prompt()
                .system("질문에 대한 답변을 한국어로 친절하게 답변해야 합니다.")
                .user(question)
                //.tools(new DateTimeTools())
                .call()
                .content();
    }

    // 예시: Prompt 입력 가능
    public String chat2(String question) {
        String answer = chatClient.prompt()
                .system("질문에 대한 답변을 한국어로 친절하게 답변해야 합니다.")
                .user(question)
                .options(this.chatOptions)
                .call()
                .content();
        return answer;
    }

}
