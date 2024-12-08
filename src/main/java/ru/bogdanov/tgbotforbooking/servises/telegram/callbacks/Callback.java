package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks;

public class Callback {

    private final CallbackTypes callbackType;

    public Callback(CallbackTypes callbackType) {
        this.callbackType = callbackType;
    }

    public CallbackTypes getCallbackType() {
        return callbackType;
    }
}
