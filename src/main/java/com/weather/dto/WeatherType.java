package com.weather.dto;

public enum WeatherType {
    THUNDERSTORM(2, "thunderstorm.png"),
    DRIZZLE(3, "drizzle.png"),
    RAIN(5, "rain.png"),
    SNOW(6, "snow.png"),
    ATMOSPHERE(7, "atmosphere.png"),
    CLOUDS(8, "clouds.png"),
    CLEAR(80, "clear.png"),

    DEFAULT(-1, "error.png");

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