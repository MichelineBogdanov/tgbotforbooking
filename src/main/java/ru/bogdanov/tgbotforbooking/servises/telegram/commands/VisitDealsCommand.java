package ru.bogdanov.tgbotforbooking.servises.telegram.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bogdanov.tgbotforbooking.servises.telegram.JsonHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.util.ArrayList;
import java.util.List;

@Component
public class VisitDealsCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(MessagesText.VISIT_DEALS_COMMAND_TEXT);
        addKeyboard(message);
        return message;
    }

    private void addKeyboard(SendMessage sendMessage) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(getGeneralInfoButtons(CallbackTypes.CHOOSE_DAY));
        keyboardButtonsRow.add(getGeneralInfoButtons(CallbackTypes.CANCEL_VISIT));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }

    private InlineKeyboardButton getGeneralInfoButtons(CallbackTypes type) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(type.getDescription());
        BaseCallbackData callbackData = new BaseCallbackData();
        callbackData.setType(type);
        String jsonCallback = JsonHandler.toJson(callbackData);
        inlineKeyboardButton.setCallbackData(jsonCallback);
        return inlineKeyboardButton;
    }

}
