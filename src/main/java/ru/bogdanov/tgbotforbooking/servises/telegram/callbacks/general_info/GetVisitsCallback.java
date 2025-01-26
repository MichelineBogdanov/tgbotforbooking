package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.enteties.Visit;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Consumer;

@Component
public class GetVisitsCallback implements CallbackHandler {

    private final UserVisitBotService userVisitBotService;

    public GetVisitsCallback(UserVisitBotService userVisitBotService) {
        this.userVisitBotService = userVisitBotService;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String userName = update.getCallbackQuery().getFrom().getUserName();
        List<Visit> visits = userVisitBotService.getVisitByUserName(userName);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        StringJoiner sj = new StringJoiner("\n");
        visits.forEach(visit -> sj.add(visit.getVisitDateTime().toString()));
        String text = visits.isEmpty()
                ? MessagesText.NO_VISITS
                : String.format(MessagesText.YOUR_VISITS_TEXT, sj);
        message.setText(text);
        return message;
    }

}
