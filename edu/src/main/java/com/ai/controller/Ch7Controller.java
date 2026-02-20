package com.ai.controller;

import com.ai.app.service.ch7.Ch7_ChatJdbcService;
import com.ai.app.service.ch7.Ch7_ChatPgvectorService;
import com.ai.app.service.ch7.Ch7_HotelEmbeddingModelService;
import com.ai.app.service.ch7.Ch7_TextEmbeddingModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "7. Embedding Model_7", description = "Embedding Model Controller")
@RestController
@RequestMapping("/ch7")
@Slf4j
@RequiredArgsConstructor
// Chapter 7. Embedding Model Controller
public class Ch7Controller {

    // 1. Text Embedding
    // 1. Text Embedding - add
    // 1. Text Embedding - delete
    final Ch7_TextEmbeddingModelService ch7TextEmbeddingModelService;
    // 2. Hotel Data Embedding
    // 2. Hotel Data Embedding - add
    // 2. Hotel Data Embedding - delete
    final Ch7_HotelEmbeddingModelService ch7HotelEmbeddingModelService;
    // 3. Chat Memory PGvector
    // 3. Chat Memory PGvector - delete
    // 3. Chat Memory PGvector - delete all
    final Ch7_ChatPgvectorService ch7ChatPgvectorService;
    // 4. Chat Memory JDBC
    // 4. Chat Memory JDBC - delete
    // 4. Chat Memory JDBC - delete all
    final Ch7_ChatJdbcService ch7ChatJdbcService;

    // 1. Text Embedding
    @Operation(
            summary = "1. Text Embedding",
            description = """
                ### 1. Text Embedding
            """,
            method = "POST"
    )
    @PostMapping("/text-embedding")
    public String textEmbedding(@RequestParam("prompt") String userPrompt, String section, String name) {
        log.info(userPrompt);
        return ch7TextEmbeddingModelService.similaritySearch(userPrompt).get(0).getText();
    }

    // 1. Text Embedding - add
    @Operation(
            summary = "1-1. Text Embedding - add",
            description = """
                ### 1-1. Text Embedding - add
            """,
            method = "POST"
    )
    @PostMapping("/add-texts")
    public String add() {
        return ch7TextEmbeddingModelService.addData();
    }

    // 1. Text Embedding - delete
    @Operation(
            summary = "1-2. Text Embedding - delete",
            description = """
                ### 1-2. Text Embedding - delete
            """,
            method = "POST"
    )
    @PostMapping("/delete-texts")
    public String delete() {
        return ch7TextEmbeddingModelService.deleteDate();
    }

    // 2. Hotel Data Embedding
    @Operation(
            summary = "2. Hotel Data Embedding",
            description = """
                ### 2. Hotel Data Embedding
            """,
            method = "POST"
    )
    @PostMapping("/hotel-embedding")
    public String hotelEmbedding(@RequestParam("prompt") String userPrompt, String section, String name) {
        log.info(userPrompt);
        return ch7HotelEmbeddingModelService.similaritySearch(userPrompt, section, name).get(0).getText();
    }

    // 2. Hotel Data Embedding - add
    @Operation(
            summary = "2-1. Hotel Data Embedding - add",
            description = """
                ### 2-1. Hotel Data Embedding - add
            """,
            method = "POST"
    )
    @PostMapping("/add-hotels")
    public String addHotels() {
        return ch7HotelEmbeddingModelService.addData();
    }

    // 2. Hotel Data Embedding - delete
    @Operation(
            summary = "2-2. Hotel Data Embedding - delete",
            description = """
                ### 2-2. Hotel Data Embedding - delete
            """,
            method = "POST"
    )
    @PostMapping("/delete-hotels")
    public String deleteHotels() {
        return ch7HotelEmbeddingModelService.deleteDate();
    }

    // 3. Chat Memory PGvector
    @Operation(
            summary = "3. Chat Memory PGvector",
            description = """
                ### 3. Chat Memory PGvector
            """,
            method = "POST"
    )
    @PostMapping("/chat-pgvector")
    public String chatPgvector(@RequestParam("prompt") String userPrompt, HttpSession  session) {
        return ch7ChatPgvectorService.chat(userPrompt, session.getId());
    }

    // 3. Chat Memory PGvector - delete
    @Operation(
            summary = "3-1. Chat Memory PGvector - delete",
            description = """
                ### 3-1. Chat Memory PGvector - delete
            """,
            method = "POST"
    )
    @PostMapping("/delete-chat-pgvector")
    public String deleteChatPgvector(HttpSession  session) {
        return ch7ChatPgvectorService.deleteChat(session.getId());
    }

    // 3. Chat Memory PGvector - delete all
    @Operation(
            summary = "3-2. Chat Memory PGvector - delete all",
            description = """
                ### 3-2. Chat Memory PGvector - delete all
            """,
            method = "POST"
    )
    @PostMapping("/delete-all-chat-pgvector")
    public String deleteAllChatPgvector() {
        return ch7ChatPgvectorService.deleteAllChat();
    }

    // 4. Chat Memory JDBC
    @Operation(
            summary = "4. Chat Memory JDBC",
            description = """
                ### 4. Chat Memory JDBC
            """,
            method = "POST"
    )
    @PostMapping("/chat-jdbc")
    public String chatJdbc(@RequestParam("prompt") String userPrompt, HttpSession  session) {
        return ch7ChatJdbcService.chat(userPrompt, session.getId());
    }

    // 4. Chat Memory JDBC - delete
    @Operation(
            summary = "4-1. Chat Memory JDBC - delete",
            description = """
                ### 4-1. Chat Memory JDBC - delete
            """,
            method = "POST"
    )
    @PostMapping("/delete-chat-jdbc")
    public String deleteChatJdbc(HttpSession  session) {
        return ch7ChatJdbcService.deleteChat(session.getId());
    }

    // 4. Chat Memory JDBC - delete all
    @Operation(
            summary = "4-2. Chat Memory JDBC - delete all",
            description = """
                ### 4-2. Chat Memory JDBC - delete all
            """,
            method = "POST"
    )
    @PostMapping("/delete-all-chat-jdbc")
    public String deleteAllChatJdbc() {
        return ch7ChatJdbcService.deleteAllChat();
    }
}