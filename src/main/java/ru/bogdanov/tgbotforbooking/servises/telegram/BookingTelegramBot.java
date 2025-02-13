package ru.bogdanov.tgbotforbooking.servises.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bogdanov.tgbotforbooking.configs.BotConfig;
import ru.bogdanov.tgbotforbooking.entities.User;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbacksHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandsHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.util.ArrayList;

@Component
@Slf4j
public class BookingTelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    public final CommandsHandler commandsHandler;

    public final CallbacksHandler callbacksHandler;
    public final UserVisitBotService userVisitBotService;

    static {
        log.info("Application started!");
    }

    public BookingTelegramBot(BotConfig config
            , CommandsHandler commandsHandler
            , CallbacksHandler callbacksHandler
            , UserVisitBotService userVisitBotService) {
        this.config = config;
        this.commandsHandler = commandsHandler;
        this.callbacksHandler = callbacksHandler;
        this.userVisitBotService = userVisitBotService;
        try {
            ArrayList<BotCommand> commands = new ArrayList<>() {{
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
                String userName = update.getMessage().getFrom().getFirstName();
                String userTgAccount = update.getMessage().getFrom().getUserName();
                if (!userVisitBotService.isUserExistsByTgAccount(userTgAccount)) {
                    User user = new User();
                    user.setName(userName);
                    user.setTgAccount(userTgAccount);
                    userVisitBotService.createUser(user);
                }
                sendMessage(commandsHandler.handleCommands(update));
            } else {
                sendMessage(new SendMessage(chatId, MessagesText.UNKNOWN_COMMAND_TEXT));
            }
        } else if (update.hasCallbackQuery()) {
            Message message = update.getCallbackQuery().getMessage();
            sendMessage(callbacksHandler.handleCallbacks(update));
            deleteMessage(message.getChatId().toString(), message.getMessageId());
            sendAnswerCallback(update.getCallbackQuery().getId());
        }
    }

    private void sendAnswerCallback(String chatId) {
        try {
            execute(new AnswerCallbackQuery(chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
