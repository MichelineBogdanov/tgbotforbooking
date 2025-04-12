package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.cancel_visit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.servises.google.GoogleAPI;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.ChooseCancelVisitCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.util.List;

@Component
public class CancelVisitCallback implements CallbackHandler {

    private final GoogleAPI service;

    public CancelVisitCallback(@Qualifier("googleCalendarService") GoogleAPI service) {
        this.service = service;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        Long tgUserId = update.getCallbackQuery().getFrom().getId();
        List<Visit> visits = service.getUserVisits(tgUserId);
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(visits.isEmpty()
                ? MessagesText.NO_VISITS_TEXT
                : MessagesText.YOUR_VISITS_FOR_CANCEL_TEXT);

        KeyboardBuilder keyboardBuilder = new KeyboardBuilder();
        for (Visit visit : visits) {
            ChooseCancelVisitCallbackData callbackData = new ChooseCancelVisitCallbackData();
            callbackData.setType(CallbackTypes.CHOOSE_CANCEL_VISIT);
            callbackData.setVisitId(visit.getId());
            keyboardBuilder.addButton(DateTimeUtils.fromLocalDateTimeToDateTimeString(visit.getVisitDateTime()), callbackData);
            keyboardBuilder.goToNewLine();
        }
        InlineKeyboardMarkup keyboardMarkup = keyboardBuilder.addBackButton(CommandTypes.VISIT_DEALS).build();
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

}
