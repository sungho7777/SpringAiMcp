package com.ai.app.dto.ch2;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "지역, 구분")
public record Contents(String location, List<Shop> shops) {

}
