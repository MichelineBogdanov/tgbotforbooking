package ru.bogdanov.tgbotforbooking.servises.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bogdanov.tgbotforbooking.entities.User;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbacksHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandsHandler;

@Component
@Slf4j
public class BookingTelegramBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;

    public final CommandsHandler commandsHandler;

    public final CallbacksHandler callbacksHandler;

    public final UserVisitBotService userVisitBotService;

    public BookingTelegramBot(CommandsHandler commandsHandler
            , CallbacksHandler callbacksHandler
            , UserVisitBotService userVisitBotService) {
        this.commandsHandler = commandsHandler;
        this.callbacksHandler = callbacksHandler;
        this.userVisitBotService = userVisitBotService;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            if (update.getMessage().getText().startsWith("/")) {
                org.telegram.telegrambots.meta.api.objects.User from = update.getMessage().getFrom();
                if (!userVisitBotService.isUserExistsByTgAccount(from.getUserName())) {
                    User user = new User();
                    user.setTgAccount(from.getUserName());
                    user.setFirstName(from.getFirstName());
                    user.setLastName(from.getLastName());
                    user.setTgUserId(from.getId());
                    user.setChatId(update.getMessage().getChatId());
                    userVisitBotService.createUser(user);
                }
                sendMessage(commandsHandler.handleCommands(update));
            } else {
                sendMessage(new SendMessage(chatId, CommandTypes.UNKNOWN_COMMAND.getDescription()));
            }
        } else if (update.hasCallbackQuery()) {
            Message message = update.getCallbackQuery().getMessage();
            sendMessage(callbacksHandler.handleCallbacks(update));
            deleteMessage(new DeleteMessage(message.getChatId().toString(), message.getMessageId()));
            sendAnswerCallback(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
        }
    }

    private void sendAnswerCallback(AnswerCallbackQuery answerCallbackQuery) {
        try {
            execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void deleteMessage(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
