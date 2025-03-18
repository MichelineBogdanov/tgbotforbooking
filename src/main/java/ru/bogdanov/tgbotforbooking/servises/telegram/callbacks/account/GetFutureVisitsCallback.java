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
import java.util.StringJoiner;

@Component
public class GetFutureVisitsCallback implements CallbackHandler {

    private final UserVisitBotService userVisitBotService;

    public GetFutureVisitsCallback(UserVisitBotService userVisitBotService) {
        this.userVisitBotService = userVisitBotService;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String userName = update.getCallbackQuery().getFrom().getUserName();
        List<Visit> visits = userVisitBotService.getFutureVisitsByUserName(userName);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        StringJoiner sj = new StringJoiner("\n");
        visits.forEach(visit -> sj.add(DateTimeUtils.fromLocalDateTimeToDateTimeString(visit.getVisitDateTime()))
                .add(" - ")
                .add(visit.getCosmetologyService().getName()));
        String text = visits.isEmpty()
                ? MessagesText.NO_VISITS_TEXT
                : String.format(MessagesText.YOUR_FUTURE_VISITS_TEXT, sj);
        message.setText(text);

        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder().addBackButton(CommandTypes.ACCOUNT).build();
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
