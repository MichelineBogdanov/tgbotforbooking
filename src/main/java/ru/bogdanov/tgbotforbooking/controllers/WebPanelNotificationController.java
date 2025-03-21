package ru.bogdanov.tgbotforbooking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;

@Controller
@RequestMapping("/notification")
public class WebPanelNotificationController extends AbstractWebPanelController{

    public WebPanelNotificationController(UserVisitBotService userVisitBotService) {
        super(userVisitBotService);
    }

    @GetMapping
    public String getNotificationSettings() {

        return "notification/notification";
    }

}
