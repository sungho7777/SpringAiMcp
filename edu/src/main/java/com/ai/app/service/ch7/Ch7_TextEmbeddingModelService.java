package com.ai.app.service.ch7;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class Ch7_TextEmbeddingModelService {
    private final VectorStore vectorStore;

    List<Document> documents = List.of(
            new Document("출근 시간은 9시 입니다.", Map.of("key", "regulation")),
            new Document("점심 시간은 11시 20분 부터 13시까지 입니다.", Map.of("key", "regulation")),
            new Document("퇴근 시간은 6시 입니다.", Map.of("key", "regulation")),
            new Document("특근은 없습니다.", Map.of("key", "regulation")),
            new Document("주말근무 없습니다.", Map.of("key", "regulation")),
            new Document("야근은 없습니다.", Map.of("key", "regulation")));

    // Constructor
    public Ch7_TextEmbeddingModelService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    // Text Embbeding 후 Vectore Store에 저장
    // Text Embbeding 시 OpenAI Embedding Models(text-embedding-ada-002)을 기본으로 사용
    public String addData(){
        vectorStore.add(documents);
        return " Add Completed";
    }
    public String deleteDate(){
        vectorStore.delete("key == 'regulation'");
        return "Delete Completed ";
    }

    public List<Document> similaritySearch(String question){
        return vectorStore.similaritySearch(question);
    }


}
