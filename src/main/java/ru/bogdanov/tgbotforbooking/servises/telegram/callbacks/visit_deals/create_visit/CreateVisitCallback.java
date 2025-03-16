package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.servises.google.CreateVisitResult;
import ru.bogdanov.tgbotforbooking.servises.google.GoogleAPI;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.CreateVisitCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class CreateVisitCallback implements CallbackHandler {

    private final GoogleAPI service;

    public CreateVisitCallback(@Qualifier("googleCalendarService") GoogleAPI service) {
        this.service = service;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        CreateVisitCallbackData currentCallback = (CreateVisitCallbackData) callback;
        LocalDate date = currentCallback.getDate();
        LocalTime time = currentCallback.getTime();
        Long serviceId = currentCallback.getServiceId();
        String userName = update.getCallbackQuery().getFrom().getUserName();
        CreateVisitResult result = service.createVisit(date, time, userName, serviceId);

        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(result.resultMessage());

        KeyboardBuilder keyboardBuilder = new KeyboardBuilder();
        InlineKeyboardMarkup keyboardMarkup = keyboardBuilder.addBackButton(CommandTypes.VISIT_DEALS).build();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
