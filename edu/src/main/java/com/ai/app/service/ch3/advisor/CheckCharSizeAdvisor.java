package com.ai.app.service.ch3.advisor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.core.Ordered;

// Question을 LLM에 전달하기 이전에 동작, Stream이 아닌 call 함수 동작시 호출
// 입력 받은 Question의  단어 길이가 2개 미만이면 예외 상황 발생
// 예외 상황은 GlobalExceptionHandler에서 처리
@Slf4j
public class CheckCharSizeAdvisor implements CallAdvisor {

    @SneakyThrows
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        this.checkCharSize(chatClientRequest);
        return callAdvisorChain.nextCall(chatClientRequest);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private void checkCharSize(ChatClientRequest chatClientRequest) throws Exception {

        if( chatClientRequest.prompt().getUserMessage().getText().length() < 2){
            throw new Exception("Char size too short");
        }
    }
}
