package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info;

import java.time.LocalDateTime;

public class LocalDateTimePeriod {

    private LocalDateTime start;
    private LocalDateTime end;

    public LocalDateTimePeriod(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "LocalDateTimePeriod{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
