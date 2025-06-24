package ru.bogdanov.tgbotforbooking.services.telegram.commands;

public enum CommandTypes {

    START("/start", "Начать работу с ботом"),
    GENERAL_INFO("/generalinfo", "Получение информации и номерках/месту приема"),
    VISIT_DEALS("/visitdeals", "Запись на прием/отмена записи"),
    ACCOUNT("/account", "Личный кабинет"),
    UNKNOWN_COMMAND("", "Неизвестная команда");

    private final String command;

    private final String description;

    CommandTypes(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public static CommandTypes fromCommandString(String commandString) {
        for (CommandTypes command : CommandTypes.values()) {
            if (command.getCommand().equalsIgnoreCase(commandString)) {
                return command;
            }
        }
        return CommandTypes.UNKNOWN_COMMAND;
    }
}
