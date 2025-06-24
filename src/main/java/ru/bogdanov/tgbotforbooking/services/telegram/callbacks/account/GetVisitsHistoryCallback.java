package ru.bogdanov.tgbotforbooking.services.telegram.callbacks.account;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.services.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.services.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.services.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.services.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.services.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.services.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.services.telegram.utils.MessagesText;

import java.util.Comparator;
import java.util.List;

@Component
public class GetVisitsHistoryCallback implements CallbackHandler {

    private final UserVisitBotService userVisitBotService;

    public GetVisitsHistoryCallback(UserVisitBotService userVisitBotService) {
        this.userVisitBotService = userVisitBotService;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long tgUserId = update.getCallbackQuery().getFrom().getId();
        List<Visit> visits = userVisitBotService.getVisitsHistoryByTgUserId(tgUserId);
        visits.sort(Comparator.comparing(Visit::getVisitDateTime));

        StringBuilder sb = new StringBuilder();
        visits.forEach(visit -> sb.append(DateTimeUtils.fromLocalDateTimeToDateTimeString(visit.getVisitDateTime()))
                .append(" - ")
                .append(visit.getCosmetologyService() == null
                        ? MessagesText.NO_SERVICE_CHOOSE_TEXT
                        : visit.getCosmetologyService().getName())
                .append("\n"));
        String text = visits.isEmpty()
                ? MessagesText.NO_VISITS_TEXT
                : String.format(MessagesText.YOUR_VISITS_HISTORY_TEXT, sb);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder().addBackButton(CommandTypes.ACCOUNT).build();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
