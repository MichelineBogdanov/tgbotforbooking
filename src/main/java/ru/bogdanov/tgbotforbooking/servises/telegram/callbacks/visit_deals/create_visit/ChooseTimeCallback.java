package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit;

import com.google.api.client.util.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.entities.CosmetologyService;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.google.CalendarAPI;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.ChooseTimeCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.CreateVisitCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ChooseTimeCallback implements CallbackHandler {

    private final CalendarAPI service;
    private final UserVisitBotService userVisitBotService;

    public ChooseTimeCallback(@Qualifier("googleCalendarService") CalendarAPI service
            , UserVisitBotService userVisitBotService) {
        this.service = service;
        this.userVisitBotService = userVisitBotService;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        ChooseTimeCallbackData currentCallback = (ChooseTimeCallbackData) callback;
        Long serviceId = currentCallback.getServiceId();
        CosmetologyService cosmetologyService = null;
        if (serviceId != null) {
            cosmetologyService = userVisitBotService.getServiceById(serviceId).get();
        }

        LocalDate date = currentCallback.getDate();
        String start = date.equals(LocalDate.now())
                ? ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                : date.atStartOfDay(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String end = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        List<LocalTime> freeSlots = service.getFreeSlots(new DateTime(start)
                , new DateTime(end)
                , cosmetologyService == null ? 30 : cosmetologyService.getDuration());

        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(String.format(MessagesText.CHOOSE_TIME_TEXT
                , cosmetologyService != null
                        ? String.join(" : ", cosmetologyService.getName(), cosmetologyService.getPrice().toString())
                        : MessagesText.NO_SERVICE_CHOOSE_TEXT
                , DateTimeUtils.fromLocalDateToDateString(date)));

        KeyboardBuilder keyboardBuilder = new KeyboardBuilder();
        for (LocalTime freeSlot : freeSlots) {
            CreateVisitCallbackData callbackData = new CreateVisitCallbackData();
            callbackData.setType(CallbackTypes.CREATE_VISIT);
            callbackData.setServiceId(serviceId);
            callbackData.setTime(freeSlot);
            callbackData.setDate(date);
            keyboardBuilder.addButton(freeSlot.toString(), callbackData);
        }
        keyboardBuilder.addBackButton(CommandTypes.VISIT_DEALS);
        InlineKeyboardMarkup keyboardMarkup = keyboardBuilder.build();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
