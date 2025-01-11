package ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;

public class BaseCallbackData {

    @JsonProperty("type")
    protected CallbackTypes type;

    public BaseCallbackData() {
    }

    public CallbackTypes getType() {
        return type;
    }

    public void setType(CallbackTypes type) {
        this.type = type;
    }
}
