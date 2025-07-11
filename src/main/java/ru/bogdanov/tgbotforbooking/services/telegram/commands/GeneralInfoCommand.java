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
public class GeneralInfoCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        String messageText = String.format(MessagesText.GENERAL_INFO_COMMAND_TEXT);
        Long chatId = update.getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);

        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder()
                .addButton(CallbackTypes.GET_SCHEDULE.getDescription(), new BaseCallbackData(CallbackTypes.GET_SCHEDULE))
                .goToNewLine()
                .addButton(CallbackTypes.GET_SERVICES_LIST_INFO.getDescription(), new BaseCallbackData(CallbackTypes.GET_SERVICES_LIST_INFO))
                .goToNewLine()
                .addButton(CallbackTypes.GET_PLACE_INFO.getDescription(), new BaseCallbackData(CallbackTypes.GET_PLACE_INFO))
                .build();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
