package ru.bogdanov.tgbotforbooking.servises.telegram.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

@Component
public class VisitDealsCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(MessagesText.VISIT_DEALS_COMMAND_TEXT);

        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder()
                .addButton(CallbackTypes.CHOOSE_DAY.getDescription(), new BaseCallbackData(CallbackTypes.CHOOSE_DAY))
                .addButton(CallbackTypes.CANCEL_VISIT.getDescription(), new BaseCallbackData(CallbackTypes.CANCEL_VISIT))
                .build();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
