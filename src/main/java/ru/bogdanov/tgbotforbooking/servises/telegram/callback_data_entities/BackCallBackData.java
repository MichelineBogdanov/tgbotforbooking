package ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;

public class BackCallBackData extends BaseCallbackData {

    @JsonProperty("command")
    protected CommandTypes command;

    public BackCallBackData() {
    }

    public CommandTypes getCommand() {
        return command;
    }

    public void setCommand(CommandTypes command) {
        this.command = command;
    }
}
