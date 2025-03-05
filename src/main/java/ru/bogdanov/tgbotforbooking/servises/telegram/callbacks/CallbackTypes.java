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
    @JsonProperty("CHOOSE_CANCEL_VISIT")
    CHOOSE_CANCEL_VISIT("Отменить визит"),
    @JsonProperty("CANCEL_VISIT")
    CANCEL_VISIT("Отменить визит"),

    // GENERAL_INFO
    @JsonProperty("GET_VISITS")
    GET_VISITS("Ваши записи"),
    @JsonProperty("GET_SCHEDULE")
    GET_SCHEDULE("Расписание"),
    @JsonProperty("GET_PLACE_INFO")
    GET_PLACE_INFO("Место приема"),

    // COMMON
    @JsonProperty("BACK")
    BACK("Назад"),

    // NOTIFICATIONS
    @JsonProperty("NOTIFICATIONS_SWITCH")
    NOTIFICATIONS_SWITCH("");

    private final String description;

    CallbackTypes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
