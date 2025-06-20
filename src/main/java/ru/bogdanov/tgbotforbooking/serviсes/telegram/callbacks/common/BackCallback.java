package ru.bogdanov.tgbotforbooking.serviсes.telegram.callbacks.common;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.callback_data_entities.BackCallbackData;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.commands.CommandsHandler;

@Component
public class BackCallback implements CallbackHandler {

    private final CommandsHandler commandsHandler;

    public BackCallback(CommandsHandler commandsHandler) {
        this.commandsHandler = commandsHandler;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        BackCallbackData currentCallback = (BackCallbackData) callback;
        CommandTypes command = currentCallback.getCommand();
        update.setMessage(update.getCallbackQuery().getMessage());
        return commandsHandler.handleCommands(command, update);
    }

}
