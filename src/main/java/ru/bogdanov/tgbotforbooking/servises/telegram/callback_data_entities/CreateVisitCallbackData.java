package ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateVisitCallbackData extends BaseCallbackData {

    @JsonProperty("date")
    @JsonSerialize(as = LocalDate.class)
    @JsonDeserialize(as = LocalDate.class)
    protected LocalDate date;

    @JsonProperty("time")
    @JsonSerialize(as = LocalTime.class)
    @JsonDeserialize(as = LocalTime.class)
    protected LocalTime time;

    public CreateVisitCallbackData() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
