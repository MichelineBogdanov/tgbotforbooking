package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit;

import com.google.api.client.util.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.servises.google.GoogleAPI;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.ChooseTimeCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ChooseDayCallback implements CallbackHandler {

    private final GoogleAPI service;

    public ChooseDayCallback(@Qualifier("googleCalendarService") GoogleAPI service) {
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
        message.setText(freeDays.isEmpty() ? MessagesText.NO_FREE_DATE_TEXT : MessagesText.CHOOSE_DATE_TEXT);

        KeyboardBuilder keyboardBuilder = new KeyboardBuilder();
        for (LocalDate freeDay : freeDays) {
            ChooseTimeCallbackData callbackData = new ChooseTimeCallbackData();
            callbackData.setType(CallbackTypes.CHOOSE_TIME);
            callbackData.setDate(freeDay);
            String text = DateTimeUtils.fromLocalDateToDateString(freeDay);
            keyboardBuilder.addButton(text, callbackData);
        }
        keyboardBuilder.addBackButton(CommandTypes.VISIT_DEALS);
        InlineKeyboardMarkup keyboardMarkup = keyboardBuilder.build();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
