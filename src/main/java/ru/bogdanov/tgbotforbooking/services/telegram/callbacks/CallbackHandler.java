package ru.bogdanov.tgbotforbooking.services.telegram.callbacks;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.services.telegram.callback_data_entities.BaseCallbackData;

public interface CallbackHandler {

    SendMessage apply(BaseCallbackData callback, Update update);

}
