package com.weather.services;

import com.weather.models.History;
import com.weather.repositories.HistoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void saveHistory(double latitude, double longitude, String description, String city, int weatherCode) {
        historyRepository.save(new History(latitude, longitude, description, LocalDateTime.now(), city, weatherCode));
    }

    public List<History> getHistory() {
        return historyRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }
}