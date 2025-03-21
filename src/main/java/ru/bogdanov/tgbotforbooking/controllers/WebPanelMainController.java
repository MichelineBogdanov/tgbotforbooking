package ru.bogdanov.tgbotforbooking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;

@Controller
@RequestMapping("/")
public class WebPanelMainController extends AbstractWebPanelController {

    public WebPanelMainController(UserVisitBotService userVisitBotService) {
        super(userVisitBotService);
    }

    @GetMapping
    public String showMainPage(Model model) {
        return "index";
    }

}
