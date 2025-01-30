package ru.bogdanov.tgbotforbooking.servises.telegram.utils;

import java.time.LocalDateTime;

public record LocalDateTimePeriod(LocalDateTime start, LocalDateTime end) {

    @Override
    public String toString() {
        return "LocalDateTimePeriod{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
