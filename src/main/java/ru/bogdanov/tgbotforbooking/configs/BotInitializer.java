package ru.bogdanov.tgbotforbooking.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.bogdanov.tgbotforbooking.serviсes.telegram.commands.CommandTypes;

import java.util.ArrayList;

@Component
public class BotInitializer {

    private final TelegramBot telegramBot;

    @Autowired
    public BotInitializer(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot((LongPollingBot) telegramBot);
            ArrayList<BotCommand> commands = new ArrayList<>() {{
                add(new BotCommand(CommandTypes.START.getCommand(), CommandTypes.START.getDescription()));
                add(new BotCommand(CommandTypes.GENERAL_INFO.getCommand(), CommandTypes.GENERAL_INFO.getDescription()));
                add(new BotCommand(CommandTypes.ACCOUNT.getCommand(), CommandTypes.ACCOUNT.getDescription()));
                add(new BotCommand(CommandTypes.VISIT_DEALS.getCommand(), CommandTypes.VISIT_DEALS.getDescription()));
            }};
            ((TelegramLongPollingBot) telegramBot).execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
