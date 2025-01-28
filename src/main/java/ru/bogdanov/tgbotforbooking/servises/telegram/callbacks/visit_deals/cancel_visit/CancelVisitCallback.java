package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.cancel_visit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.enteties.Visit;
import ru.bogdanov.tgbotforbooking.servises.google.GoogleAPI;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
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
        String userName = update.getCallbackQuery().getFrom().getUserName();
        List<Visit> deletedVisit = service.deleteVisit(userName);
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        StringJoiner sj = new StringJoiner("\n");
        deletedVisit.forEach(visit -> sj.add(DateTimeUtils.fromLocalDateTimeToDateTimeString(visit.getVisitDateTime())));
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(String.format(MessagesText.SUCCESS_CANCEL, sj));
        return message;
    }

}
