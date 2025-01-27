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
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.CreateVisitCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChooseTimeCallback implements CallbackHandler {

    private final GoogleAPI service;

    public ChooseTimeCallback(@Qualifier("googleCalendarService") GoogleAPI service) {
        this.service = service;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        ChooseTimeCallbackData currentCallback = (ChooseTimeCallbackData) callback;
        LocalDate date = currentCallback.getDate();
        String start = date.atStartOfDay(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String end = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        List<LocalTime> freeSlots = service.getFreeSlots(new DateTime(start), new DateTime(end));
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(String.format(MessagesText.CHOOSE_TIME, date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        addKeyboard(message, date, freeSlots);
        return message;
    }

    private void addKeyboard(SendMessage sendMessage, LocalDate date, List<LocalTime> times) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (LocalTime slot : times) {
            keyboardButtonsRow.add(getDateInfoButtons(date, slot));
        }
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }

    private InlineKeyboardButton getDateInfoButtons(LocalDate date, LocalTime time) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(time.toString());
        CreateVisitCallbackData callbackData = new CreateVisitCallbackData();
        callbackData.setType(CallbackTypes.CREATE_VISIT);
        callbackData.setTime(time);
        callbackData.setDate(date);
        String jsonCallback = JsonHandler.toJson(callbackData);
        inlineKeyboardButton.setCallbackData(jsonCallback);
        return inlineKeyboardButton;
    }

}
