package com.ai.app.service.ch8;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class Ch8_RetrievalAugmentationAdvisorService {

    private final ChatClient chatClient;
    private final Advisor retrievalAugmentationAdvisor;

    //Constructor
    public Ch8_RetrievalAugmentationAdvisorService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {

        SimpleLoggerAdvisor customLogger = new SimpleLoggerAdvisor(
                request -> "[SimpleLoggerAdvisor] Custom request: " + request.prompt().getUserMessage(),
                response -> "[SimpleLoggerAdvisor] Custom response: " + response.getResult(),
                0);

        this.chatClient = chatClientBuilder
                .defaultAdvisors(customLogger)
                .build();
        retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .topK(3)
                        .similarityThreshold(0.6)
                        .vectorStore(vectorStore)
                        .build())
                // vector store에 없는 경우에도  LLM에 전송을 하도록 함.
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(true)
                        .build())
                .build();
    }

    public Flux<String> ragChat(String question, String type) {

        return this.chatClient.prompt()
                .system("친절하게 한국어로 답변해줘.")
                .user(question)
                .advisors(retrievalAugmentationAdvisor)
                .advisors(a -> a.param(VectorStoreDocumentRetriever.FILTER_EXPRESSION, "type == '%s'".formatted(type)))
                .stream()
                .content();
    }

}