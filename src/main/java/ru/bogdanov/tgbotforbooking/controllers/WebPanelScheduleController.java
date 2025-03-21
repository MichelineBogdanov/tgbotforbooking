package ru.bogdanov.tgbotforbooking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;

@Controller
@RequestMapping("/schedule")
public class WebPanelScheduleController extends AbstractWebPanelController {

    public WebPanelScheduleController(UserVisitBotService userVisitBotService) {
        super(userVisitBotService);
    }

    @GetMapping
    public String getSchedule() {
        return null;
    }

}
