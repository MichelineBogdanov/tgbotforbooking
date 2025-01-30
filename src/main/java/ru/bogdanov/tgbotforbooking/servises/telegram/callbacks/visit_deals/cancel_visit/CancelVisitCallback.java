package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.cancel_visit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.servises.google.GoogleAPI;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.util.List;
import java.util.StringJoiner;

@Component
public class CancelVisitCallback implements CallbackHandler {

    private final GoogleAPI service;

    public CancelVisitCallback(@Qualifier("googleCalendarService") GoogleAPI service) {
        this.service = service;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        // Удаляем визиты
        String userName = update.getCallbackQuery().getFrom().getUserName();
        List<Visit> deletedVisits = service.deleteVisit(userName);

        long chatId = update.getCallbackQuery().getMessage().getChatId();
        StringJoiner sj = new StringJoiner("\n");
        deletedVisits.forEach(visit -> sj.add(DateTimeUtils.fromLocalDateTimeToDateTimeString(visit.getVisitDateTime())));

        // Создаем сообщение
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(String.format(MessagesText.SUCCESS_CANCEL, sj));

        // Клавиатура
        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder().addBackButton(CommandTypes.VISIT_DEALS).build();
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

}
