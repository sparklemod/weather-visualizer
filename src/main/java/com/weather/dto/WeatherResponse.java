package com.weather.dto;

public class WeatherResponse {
    private final String city;
    private final String description;
    private final Double temp;
    private final Double feelsLike;
    private final Integer weatherId;

    public WeatherResponse(String city, String description, Double temp, Double feelsLike, Integer weatherId) {
        this.city = city;
        this.description = description;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.weatherId = weatherId;
    }

    public WeatherResponse() {
        this.city = null;
        this.description = "Ошибка";
        this.temp = null;
        this.feelsLike = null;
        this.weatherId = null;
    }

    public String getCity() { return city; }
    public String getDescription() { return description; }
    public Double getTemp() { return temp; }
    public Double getFeelsLike() { return feelsLike; }
    public Integer getWeatherId() { return weatherId; }
}