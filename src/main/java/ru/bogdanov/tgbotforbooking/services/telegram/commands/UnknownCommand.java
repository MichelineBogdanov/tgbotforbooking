package ru.bogdanov.tgbotforbooking.services.telegram.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UnknownCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        Long chatId = update.getMessage().getChatId();
        return new SendMessage(chatId.toString(), CommandTypes.UNKNOWN_COMMAND.getDescription());
    }

}
