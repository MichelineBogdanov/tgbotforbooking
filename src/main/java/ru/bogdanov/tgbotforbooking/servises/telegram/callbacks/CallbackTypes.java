package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CallbackTypes {

    // VISIT_DEALS
    @JsonProperty("GET_FREE_NUMBER")
    GET_FREE_NUMBER,
    @JsonProperty("CREATE_VISIT")
    CREATE_VISIT,
    @JsonProperty("CHOOSE_DAY")
    CHOOSE_DAY,
    @JsonProperty("CHOOSE_TIME")
    CHOOSE_TIME,
    @JsonProperty("CANCEL_VISIT")
    CANCEL_VISIT,

    // GENERAL_INFO
    @JsonProperty("GET_SCHEDULE")
    GET_SCHEDULE,
    @JsonProperty("GET_PLACE_INFO")
    GET_PLACE_INFO;

}
