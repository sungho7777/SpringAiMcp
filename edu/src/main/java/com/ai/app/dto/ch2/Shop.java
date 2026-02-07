package com.ai.app.dto.ch2;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "이름, 설명, 주소, 좌표")
public record Shop(String name, String description, String address, double latitude, double longitude, List<String> menu) {

}
