package ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalTime;

public class CreateVisitCallbackData extends ChooseTimeCallbackData {

    @JsonProperty("t")
    @JsonSerialize(as = LocalTime.class)
    @JsonDeserialize(as = LocalTime.class)
    protected LocalTime time;

    public CreateVisitCallbackData() {
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
