package ru.bogdanov.tgbotforbooking.services.telegram.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.services.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.services.telegram.callbacks.CallbackTypes;
import ru.bogdanov.tgbotforbooking.services.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.services.telegram.utils.MessagesText;

@Component
public class VisitDealsCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(MessagesText.VISIT_DEALS_COMMAND_TEXT);

        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder()
                .addButton(CallbackTypes.CHOOSE_SERVICE.getDescription(), new BaseCallbackData(CallbackTypes.CHOOSE_SERVICE))
                .goToNewLine()
                .addButton(CallbackTypes.CANCEL_VISIT.getDescription(), new BaseCallbackData(CallbackTypes.CANCEL_VISIT))
                .build();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
