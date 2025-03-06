package com.weather.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
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

    private H3 cityInfo = new H3();
    private H2 weatherDescription = new H2();
    private H3 temperatureInfo = new H3();
    private Image weatherImage = new Image();
    private final Div historyContainer = new Div();

    private WeatherService weatherService;
    private HistoryService historyService;

    private BeanValidationBinder<WeatherResponse> binder;

    public WeatherView(@Autowired WeatherService weatherService, @Autowired HistoryService historyService) {
        this.weatherService = weatherService;
        this.historyService = historyService;

        NumberField latitudeField = new NumberField("Широта");
        NumberField longitudeField = new NumberField("Долгота");

        latitudeField.setPlaceholder("59.57");
        longitudeField.setPlaceholder("30.19");

        Button showWeatherButton = new Button("Показать погоду");
        Span errorMessage = new Span();

        FormLayout formLayout = new FormLayout(latitudeField, longitudeField, showWeatherButton);
        formLayout.setMaxWidth("500px");
        formLayout.getStyle().set("margin", "0 auto");

        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP)
        );

        errorMessage.getStyle().set("color", "var(--lumo-error-text-color)");
        errorMessage.getStyle().set("padding", "15px 0");

        VerticalLayout outputLayout = new VerticalLayout(cityInfo, weatherDescription, temperatureInfo, weatherImage);
        outputLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        outputLayout.setSpacing(true);

        VerticalLayout searchWeatherArea = new VerticalLayout(formLayout, outputLayout);
        searchWeatherArea.setWidth("100%");
        searchWeatherArea.setPadding(true);
        searchWeatherArea.setAlignItems(Alignment.CENTER);

        historyContainer.setWidthFull();
        historyContainer.setHeight("80%");
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
        loadHistory();

        binder = new BeanValidationBinder<>(WeatherResponse.class);

        binder.forField(latitudeField).asRequired().bind("latitude");
        binder.forField(longitudeField).asRequired().bind("longitude");
        binder.setStatusLabel(errorMessage);

        showWeatherButton.addClickListener(e -> {
            try {
                WeatherResponse response = weatherService.fetchWeather(
                        latitudeField.getValue(),
                        longitudeField.getValue()
                );

                updateWeatherInfo(response);

                historyService.saveHistory(
                        latitudeField.getValue(),
                        longitudeField.getValue(),
                        response.getDescription(),
                        response.getCity(),
                        response.getWeatherId()
                );

                loadHistory();
            } catch (Exception e2) {
                e2.printStackTrace();
                errorMessage.setText("Возникла ошибка, попробуйте позже");
            }
        });
    }

    private void updateWeatherInfo(WeatherResponse response) {
        weatherDescription.setText(response.getDescription());
        cityInfo.setText(response.getCity());
        temperatureInfo.setText(response.getTemp() + " градусов, ощущается как " + response.getFeelsLike());
        weatherImage.setSrc(WeatherType.fromWeatherId(response.getWeatherId()).getImageUrl());
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
}
