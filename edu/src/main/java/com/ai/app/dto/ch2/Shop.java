package com.ai.app.dto.ch2;

import java.util.List;

public record Shop(String name, String description, String address, double latitude, double longitude, List<String> menu) {

}
