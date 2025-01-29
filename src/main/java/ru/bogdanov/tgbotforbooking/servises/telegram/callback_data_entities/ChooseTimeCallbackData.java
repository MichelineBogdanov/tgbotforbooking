package ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;

import java.time.LocalDate;

public class ChooseTimeCallbackData extends BaseCallbackData {

    @JsonProperty("date")
    @JsonSerialize(as = LocalDate.class)
    @JsonDeserialize(as = LocalDate.class)
    protected LocalDate date;

    public ChooseTimeCallbackData() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
