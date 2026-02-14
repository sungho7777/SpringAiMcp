package com.ai.app.service.ch6.tools.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class ForecastWeatherService {

    public record WeatherResponse(CurrentWeatherTools.WeatherResponse.Current current) {
        public record Current(LocalDateTime time, int interval, double temperature_2m) {}
    }

    public String getForecastWeather(double latitude, double longitude){
        return RestClient.create()
                .get()
                .uri("https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&hourly=temperature_2m",  latitude, longitude)
                .retrieve().body(String.class);
    }
    public String getYesterdayWeather() throws IOException {
        // 데이터 조회 시 예외 상황 발생
        // ToolExceptionHandler에서 처리
        throw new IOException("Connection Error");
    }


}
