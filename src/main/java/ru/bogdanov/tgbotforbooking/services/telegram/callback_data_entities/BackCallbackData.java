package ru.bogdanov.tgbotforbooking.services.telegram.callback_data_entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.bogdanov.tgbotforbooking.services.telegram.commands.CommandTypes;

public class BackCallbackData extends BaseCallbackData {

    @JsonProperty("command")
    protected CommandTypes command;

    public BackCallbackData() {
    }

    public CommandTypes getCommand() {
        return command;
    }

    public void setCommand(CommandTypes command) {
        this.command = command;
    }
}
