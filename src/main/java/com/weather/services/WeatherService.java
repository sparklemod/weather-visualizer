package com.weather.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.dto.WeatherResponse;
import com.weather.dto.pojo.Root;
import com.weather.dto.pojo.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${openweathermap.api.url}")
    private String API_URL;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }

    public WeatherResponse fetchWeather(Double lat, Double lon) {
        String url = String.format(API_URL, lat, lon, apiKey);
        String jsonResponse = restTemplate.getForObject(url, String.class);
        return parseWeatherResponse(jsonResponse);
    }

    private WeatherResponse parseWeatherResponse(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Root root = mapper.readValue(json, Root.class);
            Weather weather = root.weather.getFirst();
            return new WeatherResponse(root.name, weather.description, root.main.temp, root.main.feels_like, weather.id);
        } catch (Exception e) {
            return new WeatherResponse();
        }
    }
}