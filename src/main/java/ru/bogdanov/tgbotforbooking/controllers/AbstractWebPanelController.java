package ru.bogdanov.tgbotforbooking.controllers;

import ru.bogdanov.tgbotforbooking.serviсes.bot_services.UserVisitBotService;

public abstract class AbstractWebPanelController {

    protected final UserVisitBotService userVisitBotService;

    public AbstractWebPanelController(UserVisitBotService userVisitBotService) {
        this.userVisitBotService = userVisitBotService;
    }

}
