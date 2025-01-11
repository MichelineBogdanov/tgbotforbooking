package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;

@Component
public class CancelVisitCallback implements CallbackHandler {

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        return null;
    }

}
