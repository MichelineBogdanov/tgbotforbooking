package ru.bogdanov.tgbotforbooking.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.google.GoogleCalendarService;

import java.util.List;

@Controller
@RequestMapping("/visits")
public class WebPanelVisitController extends AbstractWebPanelController {

    private final GoogleCalendarService googleCalendarService;

    public WebPanelVisitController(UserVisitBotService userVisitBotService,
                                   GoogleCalendarService googleCalendarService) {
        super(userVisitBotService);
        this.googleCalendarService = googleCalendarService;
    }

    @GetMapping
    public String getAllVisits(Model model) {
        List<Visit> visits = userVisitBotService.getAllVisits();
        model.addAttribute("visits", visits);
        return "visits/allVisits";
    }

    @DeleteMapping("/delete/{visitId}")
    public ResponseEntity<String> deleteVisit(@PathVariable Long visitId) {
        try {
            googleCalendarService.deleteVisit(visitId);
            return ResponseEntity.ok("Вы успешно удалили визит!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при удалении визита: " + e.getMessage());
        }
    }

}
