package com.ai.app.dto.ch2;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지역, 구분, 언어")
public record Question(String location, String content, String language) {

}

