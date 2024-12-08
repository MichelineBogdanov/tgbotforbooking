package ru.bogdanov.tgbotforbooking.servises.telegram.commands;

public enum CommandTypes {

    START("/start"),
    VISIT_DEALS("/visitDeals"),
    GENERAL_INFO("/generalInfo"),
    UNKNOWN_COMMAND("");

    private final String command;

    CommandTypes(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
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