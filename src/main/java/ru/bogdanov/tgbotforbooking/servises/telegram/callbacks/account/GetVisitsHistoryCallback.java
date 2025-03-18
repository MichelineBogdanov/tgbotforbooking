package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.account;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.DateTimeUtils;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

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
        String userName = update.getCallbackQuery().getFrom().getUserName();
        List<Visit> visits = userVisitBotService.getVisitsHistoryByUserName(userName);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
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
        message.setText(text);

        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder().addBackButton(CommandTypes.ACCOUNT).build();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
