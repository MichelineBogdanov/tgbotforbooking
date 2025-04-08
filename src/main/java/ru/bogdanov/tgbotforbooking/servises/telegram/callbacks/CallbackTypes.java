package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CallbackTypes {

    // VISIT_DEALS
    CREATE_VISIT("Взять номерок"),
    CHOOSE_DAY("Выбрать дату"),
    CHOOSE_TIME("Выбрать время"),
    CHOOSE_SERVICE("Записаться"),
    CHOOSE_CANCEL_VISIT("Отменить визит"),
    CANCEL_VISIT("Отменить визит"),

    // GENERAL_INFO
    GET_SERVICES_LIST_INFO("Список услуг"),
    GET_SCHEDULE("Расписание"),
    GET_PLACE_INFO("Место приема"),

    //ACCOUNT
    GET_FUTURE_VISITS("Ваши записи"),
    GET_VISITS_HISTORY("История посещений"),
    NOTIFICATIONS_SWITCH(""),

    // COMMON
    BACK("Назад");

    private final String description;

    CallbackTypes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

    @JsonCreator
    public static CallbackTypes fromValue(int value) {
        return CallbackTypes.values()[value];
    }
}
