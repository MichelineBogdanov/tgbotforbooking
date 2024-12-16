package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.servises.google.GoogleAPI;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.Callback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;

@Component
public class ScheduleInfoCallback implements CallbackHandler {

    private final GoogleAPI service;

    public ScheduleInfoCallback(@Qualifier("stubGoogleCalendarService") GoogleAPI service) {
        this.service = service;
    }

    @Override
    public SendMessage apply(Callback callback, Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(callback.getCallbackType().getDescription());
        return message;
    }

}
