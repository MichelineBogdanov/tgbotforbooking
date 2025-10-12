package ru.bogdanov.tgbotforbooking.services.telegram.commands;

import org.apache.logging.log4j.util.Strings;

public enum CommandTypes {

    START("/start",
            "Начать работу с ботом",
            Strings.EMPTY),

    GENERAL_INFO("/generalinfo",
            "Получение информации и номерках/месту приема",
            "Общая информация"),

    ACCOUNT("/account",
            "Личный кабинет",
            "Личный кабинет"),

    VISIT_DEALS("/visitdeals",
            "Запись на прием/отмена записи",
            "Управление записями"),

    UNKNOWN_COMMAND(Strings.EMPTY,
            "Неизвестная команда",
            Strings.EMPTY);

    private final String command;
    private final String description;
    private final String keyBoardDescription;

    CommandTypes(String command, String description, String keyBoardDescription) {
        this.command = command;
        this.description = description;
        this.keyBoardDescription = keyBoardDescription;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getKeyBoardDescription() {
        return keyBoardDescription;
    }

    public static CommandTypes fromCommandString(String commandString) {
        for (CommandTypes command : CommandTypes.values()) {
            if (command.getCommand().equalsIgnoreCase(commandString)
                    || command.getKeyBoardDescription().equalsIgnoreCase(commandString)) {
                return command;
            }
        }
        return CommandTypes.UNKNOWN_COMMAND;
    }

}
