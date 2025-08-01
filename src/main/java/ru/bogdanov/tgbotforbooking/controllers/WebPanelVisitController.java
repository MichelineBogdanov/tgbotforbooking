package ru.bogdanov.tgbotforbooking.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bogdanov.tgbotforbooking.dto.CreateVisitDTO;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.exceptions.CreateVisitException;
import ru.bogdanov.tgbotforbooking.services.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.services.google.CreateVisitResult;
import ru.bogdanov.tgbotforbooking.services.google.GoogleCalendarService;
import ru.bogdanov.tgbotforbooking.services.telegram.JsonHandler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
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
    public String getVisits(Model model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable paging = PageRequest.of(page - 1, size, Sort.by("visitDateTime").descending());
            Page<Visit> pageTuts = userVisitBotService.getAllVisitsPaginated(paging);
            List<Visit> content = pageTuts.getContent();
            model.addAttribute("visits", content);
            model.addAttribute("currentPage", pageTuts.getNumber() + 1);
            model.addAttribute("totalItems", pageTuts.getTotalElements());
            model.addAttribute("totalPages", pageTuts.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
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

    @PostMapping("/create")
    public ResponseEntity<?> createVisit(@RequestBody CreateVisitDTO visit) {
        try {
            LocalDate visitDate = visit.getDate();
            LocalTime visitTime = visit.getTime();
            Long tgUserId = visit.getTgUserId();
            Long serviceId = visit.getServiceId();
            CreateVisitResult result = googleCalendarService.createVisit(visitDate, visitTime, tgUserId, serviceId);
            String json = JsonHandler.toJson(result);
            return ResponseEntity.ok(json);
        } catch (CreateVisitException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Collections.singletonMap("error", "Произошла непредвиденная ошибка"));
        }
    }

}
