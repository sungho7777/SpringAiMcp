package com.ai.controller;

import com.ai.app.service.ch1.Ch1_ChatClientService;
import com.ai.app.service.ch5.Ch5_OpenAiAudioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


@Tag(name = "5. Chapter_5", description = "Multimodality API – Audio and Speech Controller")
@RestController
@RequestMapping("/ch5")
@Slf4j
@RequiredArgsConstructor
// Chapter 5. Multimodality API – Audio and Speech Controller
public class Ch5Controller {

    // 1. Text to Speech
    // 2. Text to Speech: Chat
    // 3. Text to Speech: Chat Stream
    // 4. Speech to Text
    // 5. Speech to Text: Chat
    // 6. Speech to Text: Chat Voice
    final Ch5_OpenAiAudioService ch5OpenAiAudioService;
    // 5. Speech to Text: Chat
    final Ch1_ChatClientService ch1ChatClientService;

    // 1. Text to Speech
    @Operation(
            summary = "1. Text to Speech",
            description = """
                ### 1. Text to Speech
                ### @("/generate-image-url")
                POST http://localhost:8080/ch5/text-to-speech
                Content-Type: application/x-www-form-urlencoded
                Accept: application/json;charset=UTF-8
                
                prompt=내가 하는 말을 오디오로 변환 한다고?
            """,
            method = "POST"
    )
    @PostMapping("/text-to-speech")
    public Map<String, String> textToSpeech(@RequestParam("prompt") String question) {
        log.info(question);
        return ch5OpenAiAudioService.textToSpeech(question);
    }

    // 2. Text to Speech: Chat
    @Operation(
            summary = "2. Text to Speech: Chat",
            description = """
                ### 2. Text to Speech: Chat
                ### @("/text-to-speech-chat")
                POST http://localhost:8080/ch5/text-to-speech-chat
                Content-Type: application/x-www-form-urlencoded
                Accept: application/json;charset=UTF-8
                
                prompt=너는 누구야??
            """,
            method = "POST"
    )
    @PostMapping("/text-to-speech-chat")
    public Map<String, String> textToSpeechChat(@RequestParam("prompt") String question) {
        log.info(question);
        return ch5OpenAiAudioService.textToSpeechChat(question);
    }

    // 3. Text to Speech: Chat Stream
    @Operation(
            summary = "3. Text to Speech: Chat Stream",
            description = """
                ### 3. Text to Speech: Chat Stream
                ### @("/text-to-speech-chat")
                POST http://localhost:8080/ch5/text-to-speech-chat-stream
                Content-Type: application/x-www-form-urlencoded
                Accept: application/octet-stream;charset=UTF-8
                
                prompt=너는 누구야??
            """,
            method = "POST"
    )
    @PostMapping(value = "/text-to-speech-chat-stream")
    public void textToSpeechChatStream(@RequestParam("prompt") String question, HttpServletResponse response) throws IOException {
        log.info(question);
        Flux<byte[]> bytes =  ch5OpenAiAudioService.textToSpeechChatStream(question);
        OutputStream os = response.getOutputStream();

        ByteArrayOutputStream combined = new ByteArrayOutputStream();
        for (byte[] data : bytes.toIterable()) {
            combined.write(data);
        }
        os.write(combined.toByteArray());
        os.flush();
    }

    // 4. Speech to Text
    @Operation(
            summary = "4. Speech to Text",
            description = """
                Speech to Text
            """,
            method = "POST"
    )
    @PostMapping(value = "/speech-to-text")
    public String speechToText(
            @RequestParam(value="attach", required = false) MultipartFile attach) throws IOException {
        return ch5OpenAiAudioService.speechToText(attach);
    }

    // 5. Speech to Text: Chat
    @Operation(
            summary = "5. Speech to Text: Chat",
            description = """
                Speech to Text: Chat
            """,
            method = "POST"
    )
    @PostMapping(value = "/speech-to-text-chat")
    public Flux<String> speechToTextChat(
            @RequestParam(value="attach", required = false) MultipartFile attach) throws IOException {
        // 음성 데이터를 Text로 변환 한 후 다시 LLM에 전달 하여 응답 하녀 Flux로 전달
        String text = ch5OpenAiAudioService.speechToText(attach);
        return ch1ChatClientService.chatStream(text);
    }

    // 6. Speech to Text: Chat Voice
    @Operation(
            summary = "6. Speech to Text: Chat Voice",
            description = """
                Speech to Text: Chat Voice
            """,
            method = "POST"
    )
    @PostMapping(value = "/speech-to-text-chat-voice")
    public Map<String, String> speechToTextChatVoice(
            @RequestParam(value="attach", required = false) MultipartFile attach) throws IOException {
        String text = ch5OpenAiAudioService.speechToText(attach);
        return ch5OpenAiAudioService.textToSpeechChat(text);
    }

}
