package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CallbackTypes {

    // VISIT_DEALS
    @JsonProperty("CREATE_VISIT")
    CREATE_VISIT("Взять номерок"),
    @JsonProperty("CHOOSE_DAY")
    CHOOSE_DAY("Выбрать дату для записи"),
    @JsonProperty("CHOOSE_TIME")
    CHOOSE_TIME("Выбрать время"),
    @JsonProperty("CANCEL_VISIT")
    CANCEL_VISIT("Отменить визит"),

    // GENERAL_INFO
    @JsonProperty("GET_SCHEDULE")
    GET_SCHEDULE("Получить расписание"),
    @JsonProperty("GET_PLACE_INFO")
    GET_PLACE_INFO("Получить информацию о месте");

    private final String description;

    CallbackTypes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
