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
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.util.Optional;

@Component
public class ChooseCancelVisitCallback implements CallbackHandler {

    private final GoogleAPI service;

    public ChooseCancelVisitCallback(@Qualifier("googleCalendarService") GoogleAPI service) {
        this.service = service;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        ChooseCancelVisitCallbackData currentCallbackData = (ChooseCancelVisitCallbackData) callback;
        Long id = currentCallbackData.getVisitId();
        Optional<Visit> optionalVisit = service.deleteVisit(id);
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(optionalVisit.isPresent()
                ? String.format(MessagesText.SUCCESS_CANCEL_TEXT
                , DateTimeUtils.fromLocalDateTimeToDateTimeString(optionalVisit.get().getVisitDateTime()))
                : MessagesText.NOT_SUCCESS_CANCEL_TEXT);

        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder().addBackButton(CommandTypes.VISIT_DEALS).build();
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

}
