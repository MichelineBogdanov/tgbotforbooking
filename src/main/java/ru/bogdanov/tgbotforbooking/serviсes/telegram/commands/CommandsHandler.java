package ru.bogdanov.tgbotforbooking.serviсes.telegram.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class CommandsHandler {

    private final Map<CommandTypes, Command> handler;

    public CommandsHandler(StartCommand startCommand
            , GeneralInfoCommand generalInfoCommand
            , VisitDealsCommand visitDealsCommand
            , AccountCommand accountCommand
            , UnknownCommand unknownCommand) {
        this.handler = Map.ofEntries(Map.entry(CommandTypes.START, startCommand)
                , Map.entry(CommandTypes.GENERAL_INFO, generalInfoCommand)
                , Map.entry(CommandTypes.VISIT_DEALS, visitDealsCommand)
                , Map.entry(CommandTypes.ACCOUNT, accountCommand)
                , Map.entry(CommandTypes.UNKNOWN_COMMAND, unknownCommand)
        );
    }

    public SendMessage handleCommands(Update update) {
        String text = update.getMessage().getText();
        Command command = handler.get(CommandTypes.fromCommandString(text));
        return command.apply(update);
    }

    public SendMessage handleCommands(CommandTypes commandType, Update update) {
        Command command = handler.get(commandType);
        return command.apply(update);
    }

}
