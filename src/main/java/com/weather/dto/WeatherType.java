package com.weather.dto;

public enum WeatherType {
    THUNDERSTORM(2, "thunderstorm.jpg"),
    DRIZZLE(3, "drizzle.jpg"),
    RAIN(5, "rain.jpg"),
    SNOW(6, "snow.jpg"),
    ATMOSPHERE(7, "atmosphere.jpg"),
    CLOUDS(8, "clouds.jpg"),
    CLEAR(80, "clear.jpg"),

    DEFAULT(-1, "error.jpg");

    private final int codePrefix;
    private final String imageUrl;

    WeatherType(int codePrefix, String imageUrl) {
        this.codePrefix = codePrefix;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public static WeatherType fromWeatherId(int weatherId) {
        if (weatherId == 800) {
            return CLEAR;
        }

        for (WeatherType type : values()) {
            if (type.codePrefix == weatherId / 100) {
                return type;
            }
        }

        return DEFAULT;
    }
}