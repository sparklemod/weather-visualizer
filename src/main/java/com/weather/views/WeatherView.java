package com.weather.views;


import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.NumberField;
import com.weather.models.History;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.weather.services.HistoryService;
import com.weather.dto.WeatherResponse;
import com.weather.dto.WeatherType;
import com.weather.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("")
public class WeatherView extends VerticalLayout {

    private final WeatherService weatherService;
    private final HistoryService historyService;
    private final NumberField latitudeField = new NumberField("Широта");
    private final NumberField longitudeField = new NumberField("Долгота");
    private final Button fetchWeatherButton = new Button("Показать погоду");

    private final H3 cityInfo = new H3();
    private final H2 weatherDescription = new H2();
    private final H3 temperatureInfo = new H3();
    private final Image weatherImage = new Image();

    private final Div historyContainer = new Div();

    public WeatherView(@Autowired WeatherService weatherService, @Autowired HistoryService historyService) {
        this.weatherService = weatherService;
        this.historyService = historyService;

        setupUI();
        loadHistory();
    }

    private void setupUI() {
        latitudeField.setPlaceholder("59.57");
        longitudeField.setPlaceholder("30.19");

        latitudeField.setMin(-90);
        latitudeField.setMax(90);
        longitudeField.setMin(-180);
        longitudeField.setMax(180);

        fetchWeatherButton.addClickListener(event -> fetchWeather());

        VerticalLayout inputLayout = new VerticalLayout(latitudeField, longitudeField, fetchWeatherButton);
        inputLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        inputLayout.setSpacing(true);

        VerticalLayout outputLayout = new VerticalLayout(cityInfo, weatherDescription, temperatureInfo, weatherImage);
        outputLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        outputLayout.setSpacing(true);

        VerticalLayout searchWeatherArea = new VerticalLayout(inputLayout, outputLayout);
        searchWeatherArea.setWidth("100%");
        searchWeatherArea.setPadding(true);
        searchWeatherArea.setAlignItems(Alignment.CENTER);

        historyContainer.setWidthFull();
        historyContainer.setHeight("800%");
        historyContainer.getStyle().set("overflow-y", "auto").set("border", "1px solid #ccc").set("padding", "10px");

        VerticalLayout historySection = new VerticalLayout(new H2("История"), historyContainer);
        historySection.setWidth("50%");
        historySection.setPadding(true);
        historySection.setAlignItems(Alignment.START);

        Div separator = new Div();
        separator.setWidth("2px");
        separator.setHeight("100%");
        separator.getStyle().set("background-color", "#ccc");

        HorizontalLayout contentLayout = new HorizontalLayout(searchWeatherArea, separator, historySection);
        contentLayout.setWidthFull();
        contentLayout.setHeight("100vh");

        add(contentLayout);
    }

    private void fetchWeather() {
        try {
            WeatherResponse response = weatherService.fetchWeather(latitudeField.getValue(), longitudeField.getValue());
            updateWeatherInfo(response);

            historyService.saveHistory(latitudeField.getValue(), longitudeField.getValue(), response.getDescription(), response.getCity(), response.getWeatherId());

            loadHistory();
        } catch (NumberFormatException e) {
            Notification.show("Введите корректные координаты", 3000, Notification.Position.TOP_START);
        }
    }

    private void updateWeatherInfo(WeatherResponse response) {
        weatherDescription.setText(response.getDescription());
        cityInfo.setText(response.getCity());
        temperatureInfo.setText(response.getTemp() + " градусов, ощущается как " + response.getFeelsLike());
        weatherImage.setSrc(getImageUrl(response.getWeatherId()));
        weatherImage.setMaxWidth("400px");
    }

    private void loadHistory() {
        historyContainer.removeAll();
        List<History> historyEntries = historyService.getHistory();

        for (History entry : historyEntries) {
            Div historyItem = new Div();
            historyItem.setText(entry.getTimestamp().format(DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm:ss"))
                    + " | широта: " + entry.getLatitude()
                    + ", долгота: " + entry.getLongitude()
                    + " | город: " + entry.getCity()
                    + " | ответ: " + entry.getDescription()
            );

            historyItem.getStyle().set("padding", "10px");
            historyContainer.add(historyItem);
        }
    }


    public String getImageUrl(Integer weatherId) {
        return WeatherType.fromWeatherId(weatherId).getImageUrl();
    }
}
