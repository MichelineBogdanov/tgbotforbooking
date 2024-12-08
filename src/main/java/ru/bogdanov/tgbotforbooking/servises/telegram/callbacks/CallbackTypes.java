package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks;

public enum CallbackTypes {

    // VISIT_DEALS
    GET_FREE_NUMBER("Получить свободные номерки"),
    CREATE_VISIT("Записаться на прием"),
    CANCEL_VISIT("Отменить визит"),

    // GENERAL_INFO
    GET_SCHEDULE("Получить расписание"),
    GET_PLACE_INFO("Узнать информацию о месте");

    private final String description;

    CallbackTypes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
