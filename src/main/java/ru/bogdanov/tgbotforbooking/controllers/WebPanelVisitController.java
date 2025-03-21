package ru.bogdanov.tgbotforbooking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;

import java.util.List;

@Controller
@RequestMapping("/visits")
public class WebPanelVisitController extends AbstractWebPanelController {

    public WebPanelVisitController(UserVisitBotService userVisitBotService) {
        super(userVisitBotService);
    }

    @GetMapping
    public String getAllVisits(Model model) {
        List<Visit> visits = userVisitBotService.getAllVisits();
        model.addAttribute("visits", visits);
        return "visits/allVisits";
    }

}
