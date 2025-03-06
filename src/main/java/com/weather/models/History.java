package com.weather.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double latitude;
    private Double longitude;
    private String description;
    private LocalDateTime timestamp;
    private String city;
    private Integer weatherCode;

    public History() {
    }

    public History(Double latitude, Double longitude, String description, LocalDateTime timestamp, String city, Integer weatherCode) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.timestamp = timestamp;
        this.city = city;
        this.weatherCode = weatherCode;
    }

    public Long getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getCity() {
        return city;
    }

    public Integer getWeatherCode() {
        return weatherCode;
    }
}

