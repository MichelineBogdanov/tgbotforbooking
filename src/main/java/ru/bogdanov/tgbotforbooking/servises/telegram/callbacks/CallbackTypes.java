package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CallbackTypes {

    // VISIT_DEALS
    CREATE_VISIT("Взять номерок"),
    CHOOSE_DAY("Выбрать дату для записи"),
    CHOOSE_TIME("Выбрать время"),
    CHOOSE_SERVICE("Выбрать услугу"),
    CHOOSE_CANCEL_VISIT("Отменить визит"),
    CANCEL_VISIT("Отменить визит"),

    // GENERAL_INFO
    GET_VISITS("Ваши записи"),
    GET_SCHEDULE("Расписание"),
    GET_PLACE_INFO("Место приема"),

    // COMMON
    BACK("Назад"),

    // NOTIFICATIONS
    NOTIFICATIONS_SWITCH("");

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
