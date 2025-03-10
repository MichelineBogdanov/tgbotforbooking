package ru.bogdanov.tgbotforbooking.servises.telegram.commands;

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
            , UnknownCommand unknownCommand) {
        this.handler = Map.of(CommandTypes.START, startCommand
                , CommandTypes.GENERAL_INFO, generalInfoCommand
                , CommandTypes.VISIT_DEALS, visitDealsCommand
                , CommandTypes.UNKNOWN_COMMAND, unknownCommand
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
