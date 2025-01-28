package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit;

import com.google.api.client.util.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bogdanov.tgbotforbooking.servises.google.GoogleAPI;
import ru.bogdanov.tgbotforbooking.servises.telegram.JsonHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.ChooseTimeCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChooseDayCallBack implements CallbackHandler {

    private final GoogleAPI service;

    public ChooseDayCallBack(@Qualifier("googleCalendarService") GoogleAPI service) {
        this.service = service;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        List<LocalDate> freeDays = service.getFreeDays(
                new DateTime(DateTimeUtils.fromLocalDateTimeToDate(LocalDateTime.now())),
                new DateTime(DateTimeUtils.fromLocalDateTimeToDate(LocalDate.now().plusMonths(1).withDayOfMonth(1).atStartOfDay())));
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(MessagesText.CHOOSE_DATE);
        addKeyboard(message, freeDays);
        return message;
    }

    private void addKeyboard(SendMessage sendMessage, List<LocalDate> freeDays) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (LocalDate localDate : freeDays) {
            keyboardButtonsRow.add(getDateInfoButtons(localDate));
        }
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }

    private InlineKeyboardButton getDateInfoButtons(LocalDate date) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(DateTimeUtils.fromLocalDateToDateString(date));
        ChooseTimeCallbackData callbackData = new ChooseTimeCallbackData();
        callbackData.setType(CallbackTypes.CHOOSE_TIME);
        callbackData.setDate(date);
        String jsonCallback = JsonHandler.toJson(callbackData);
        inlineKeyboardButton.setCallbackData(jsonCallback);
        return inlineKeyboardButton;
    }

}
