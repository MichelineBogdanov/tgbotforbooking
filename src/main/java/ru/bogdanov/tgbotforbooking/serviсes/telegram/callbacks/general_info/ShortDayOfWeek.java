package ru.bogdanov.tgbotforbooking.serviсes.telegram.callbacks.general_info;

public enum ShortDayOfWeek {

    MONDAY("пн"),
    TUESDAY("вт"),
    WEDNESDAY("ср"),
    THURSDAY("чт"),
    FRIDAY("пт"),
    SATURDAY("сб"),
    SUNDAY("вс");

    private final String shortName;

    ShortDayOfWeek(String shortName) {
        this.shortName = shortName;
    }

    public int getValue() {
        return ordinal() + 1;
    }

    public String getShortName() {
        return shortName;
    }

}
