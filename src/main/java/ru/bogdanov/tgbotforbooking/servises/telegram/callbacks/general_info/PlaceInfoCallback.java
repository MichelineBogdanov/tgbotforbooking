package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;

@Component
public class PlaceInfoCallback implements CallbackHandler {

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(callback.getType().toString());
        return message;
    }

}
