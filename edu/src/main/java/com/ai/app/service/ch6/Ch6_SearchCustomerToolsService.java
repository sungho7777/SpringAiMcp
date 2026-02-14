package com.ai.app.service.ch6;


import com.ai.app.service.ch6.tools.customer.CustomerStringTools;
import com.ai.app.service.ch6.tools.customer.CustomerTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Ch6_SearchCustomerToolsService {

    private final ChatClient chatClient;

    // Constructor
    public Ch6_SearchCustomerToolsService(ChatClient.Builder chatClientBuilder) {
        SimpleLoggerAdvisor customLogger = new SimpleLoggerAdvisor(
                request -> "[SimpleLoggerAdvisor] Custom request: " + request.prompt().getUserMessage(),
                response -> "[SimpleLoggerAdvisor] Custom response: " + response.getResult(),
                Ordered.HIGHEST_PRECEDENCE);
        this.chatClient = chatClientBuilder
                .defaultAdvisors(customLogger)
                .build();
    }
    public String getCustomer(String question) {
        return chatClient.prompt()
                .system("질문에 대한 답변을 한국어로 친절하게 답변해야 합니다.")
                .user(question)
                .tools(new CustomerTools())
                .call()
                .content();
    }
    public String getCustomerString(String question) {
        return chatClient.prompt()
                .system("질문에 대한 답변을 한국어로 친절하게 답변해야 합니다.")
                .user(question)
                .tools(new CustomerStringTools())
                .call()
                .content();
    }
}
