package ru.bogdanov.tgbotforbooking.servises.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bogdanov.tgbotforbooking.configs.BotConfig;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbacksHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandsHandler;

@Component
public class BookingTelegramBotService extends TelegramLongPollingBot {

    private final BotConfig config;

    public final CommandsHandler commandsHandler;

    public final CallbacksHandler callbacksHandler;

    public BookingTelegramBotService(BotConfig config, CommandsHandler commandsHandler, CallbacksHandler callbacksHandler) {
        this.config = config;
        this.commandsHandler = commandsHandler;
        this.callbacksHandler = callbacksHandler;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            if (update.getMessage().getText().startsWith("/")) {
                sendMessage(commandsHandler.handleCommands(update));
            } else {
                sendMessage(new SendMessage(chatId, "UNKNOWN_COMMAND"));
            }
        } else if (update.hasCallbackQuery()) {
            sendMessage(callbacksHandler.handleCallbacks(update));
        }
    }

    private void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
