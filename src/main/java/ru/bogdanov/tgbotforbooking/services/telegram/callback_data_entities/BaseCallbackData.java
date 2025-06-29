package ru.bogdanov.tgbotforbooking.services.telegram.callback_data_entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.bogdanov.tgbotforbooking.services.telegram.callbacks.CallbackTypes;

public class BaseCallbackData {

    @JsonProperty("type")
    protected CallbackTypes type;

    public BaseCallbackData() {
    }

    public BaseCallbackData(CallbackTypes type) {
        this.type = type;
    }

    public CallbackTypes getType() {
        return type;
    }

    public void setType(CallbackTypes type) {
        this.type = type;
    }
}
