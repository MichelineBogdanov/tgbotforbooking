package ru.bogdanov.tgbotforbooking.servises.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bogdanov.tgbotforbooking.configs.BotConfig;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbacksHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandsHandler;

import java.util.ArrayList;

@Component
public class BookingTelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    public final CommandsHandler commandsHandler;

    public final CallbacksHandler callbacksHandler;

    public BookingTelegramBot(BotConfig config, CommandsHandler commandsHandler, CallbacksHandler callbacksHandler) {
        this.config = config;
        this.commandsHandler = commandsHandler;
        this.callbacksHandler = callbacksHandler;
        try {
            ArrayList<BotCommand> commands = new ArrayList<>(){{
                add(new BotCommand(CommandTypes.START.getCommand(), "Начать работу с ботом"));
                add(new BotCommand(CommandTypes.GENERAL_INFO.getCommand(), "Получение информации и номерках/месту приема"));
                add(new BotCommand(CommandTypes.VISIT_DEALS.getCommand(), "Запись на прием/отмена записи"));
            }};
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
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

    private void deleteMessage(String chatId, int messageId) {
        try {
            execute(new DeleteMessage(chatId, messageId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
