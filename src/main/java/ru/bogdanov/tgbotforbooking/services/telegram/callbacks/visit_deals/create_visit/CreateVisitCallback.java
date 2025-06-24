package ru.bogdanov.tgbotforbooking.services.telegram.callbacks.visit_deals.create_visit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.exceptions.CreateVisitException;
import ru.bogdanov.tgbotforbooking.services.google.CreateVisitResult;
import ru.bogdanov.tgbotforbooking.services.google.CalendarAPI;
import ru.bogdanov.tgbotforbooking.services.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.services.telegram.callback_data_entities.CreateVisitCallbackData;
import ru.bogdanov.tgbotforbooking.services.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.services.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.services.telegram.utils.KeyboardBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class CreateVisitCallback implements CallbackHandler {

    private final CalendarAPI service;

    public CreateVisitCallback(@Qualifier("googleCalendarService") CalendarAPI service) {
        this.service = service;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        CreateVisitCallbackData currentCallback = (CreateVisitCallbackData) callback;
        LocalDate date = currentCallback.getDate();
        LocalTime time = currentCallback.getTime();
        Long serviceId = currentCallback.getServiceId();
        Long tgUserId = update.getCallbackQuery().getFrom().getId();

        String text;
        try {
            CreateVisitResult result = service.createVisit(date, time, tgUserId, serviceId);
            text = result.resultMessage();
        } catch (CreateVisitException e) {
            text = e.getMessage();
        }

        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        KeyboardBuilder keyboardBuilder = new KeyboardBuilder();
        InlineKeyboardMarkup keyboardMarkup = keyboardBuilder.addBackButton(CommandTypes.VISIT_DEALS).build();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
