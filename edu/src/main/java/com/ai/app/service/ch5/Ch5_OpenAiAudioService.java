package com.ai.app.service.ch5;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class Ch5_OpenAiAudioService {

    private final ChatClient chatClient;
    // 음성 파일을 Text로 변환
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    private final OpenAiAudioTranscriptionOptions textOpentions;
    //Text 파일을 음성 파일로 변환
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;
    // Text 파일을 음성 파일로 변환 할때 어떤 Model을 사용할지 설정
    private final OpenAiAudioSpeechOptions speechOptions;

    // Constructor
    public Ch5_OpenAiAudioService(ChatClient.Builder chatClientBuilder,
                                  OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel,
                                  OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        chatClient = chatClientBuilder.build();

        // 음성 파일을 Text로 변환 하기 위한 Model 설정 및 Option 설정
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
        this.textOpentions = OpenAiAudioTranscriptionOptions.builder()
                .model("whisper-1")
                .language("ko")
                .build();

        // Text 파일을 음성 파일로 변환하기 위한 Model 설정 및 Option 설정
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
        this.speechOptions = OpenAiAudioSpeechOptions.builder()
                .model("gpt-4o-mini-tts")
                .voice(OpenAiAudioApi.SpeechRequest.Voice.NOVA)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0)
                .build();;

    }

    // 음성 데이터를 입력 받아 Text로 변환
    public String speechToText(MultipartFile multipartFile) throws IOException {
        Path tempFile = Files.createTempFile("multipart-", multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);
        Resource audioResource = new FileSystemResource(tempFile);
        // Prompt 생성
        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(audioResource, textOpentions);
        // Model 호출, 호실 시 음성 파일 전송 후 Text로 답변
        AudioTranscriptionResponse response = openAiAudioTranscriptionModel.call(prompt);
        String text = response.getResult().getOutput();
        return text;
    }

    // Text 파일을 음성으로 변환
    public Map<String, String> textToSpeech(String text) {
        // Prompt 생성
        TextToSpeechPrompt speechPrompt = new TextToSpeechPrompt(text, speechOptions);
        // Model 호출, 호실 시 Text 파일 전송 후 음성 파일을 byte[]로 답변
        TextToSpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);
        byte[] bytes = response.getResult().getOutput();
        // byte []를 base64 형식의 String 으로 변환 후 전송
        String base64Audio = Base64.getEncoder().encodeToString(bytes);
        Map<String, String> result = new HashMap<>();
        result.put("audio", base64Audio);
        return result;
    }

    // Text를 음성으로 변환, 단 음성으로 변환 시 Stream을 통해 데이터를 받아 Flux로 전달
    public   Flux<byte[]>  textToSpeechChatStream(String question) {
        String answerText = chatClient.prompt()
                .system("질문에 대한 답변을 한국어로 친절하게 답변해야 합니다.")
                .user(question)
                .call()
                .content();

        return openAiAudioSpeechModel.stream(answerText, speechOptions);
    }


    //  Question을  LLM에 전달 하여 답변을 받고,  받은 답변을 음성으로 변환 다시 답변을 요청, 이후 답변에 대한 내용을 음성과 Text로 전달
    public Map<String, String> textToSpeechChat(String question) {
        // LLM에 요청 후 답변을 Text로 받음
        String answerText = chatClient.prompt()
                //.system("100자이내로 친절하게 답변해줘.")
                .system("질문에 대한 답변을 한국어로 친절하게 답변해야 합니다.")
                .user(question)
                .call()
                .content();

        //  받은 답변을 음성으로 변환
        TextToSpeechPrompt speechPrompt = new TextToSpeechPrompt(answerText, speechOptions);
        TextToSpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);

        byte[] responseAsBytes = response.getResult().getOutput();
        String base64Audio = Base64.getEncoder().encodeToString(responseAsBytes);

        // 답변 Text와 답변을 음성으로 변환하여  Map으로 전송
        Map<String, String> result = new HashMap<>();
        result.put("answer", answerText);
        result.put("audio", base64Audio);

        return result;
    }

}

