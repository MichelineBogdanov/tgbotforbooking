package ru.bogdanov.tgbotforbooking.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bogdanov.tgbotforbooking.entities.User;
import ru.bogdanov.tgbotforbooking.entities.Visit;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class WebPanelUserController extends AbstractWebPanelController {

    private static final Logger log = LoggerFactory.getLogger(WebPanelUserController.class);

    public WebPanelUserController(UserVisitBotService userVisitBotService) {
        super(userVisitBotService);
    }

    @GetMapping
    public String getUsers(Model model,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable paging = PageRequest.of(page - 1, size, Sort.by("id").ascending());
            Page<User> pageTuts = userVisitBotService.getAllUsersPaginated(paging);
            List<User> content = pageTuts.getContent();
            model.addAttribute("users", content);
            model.addAttribute("currentPage", pageTuts.getNumber() + 1);
            model.addAttribute("totalItems", pageTuts.getTotalElements());
            model.addAttribute("totalPages", pageTuts.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "users/users";
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        try {
            Optional<User> optionalUser = userVisitBotService.getUserById(userId);
            return optionalUser
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        try {
            User savedUser = userVisitBotService.updateUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/visits")
    public String getUserVisits(@RequestParam Long userId, Model model) {
        try {
            Optional<User> userOptional = userVisitBotService.getUserById(userId);
            User user = userOptional.get();
            List<Visit> visits = user.getVisits();
            visits.sort(Comparator.comparing(Visit::getVisitDateTime).reversed());
            long totalAmount = visits.stream()
                    .filter(visit -> visit.getCosmetologyService() != null)
                    .mapToLong(visit -> visit.getCosmetologyService().getPrice())
                    .sum();
            model.addAttribute("user", user);
            model.addAttribute("visits", visits);
            model.addAttribute("totalAmount", totalAmount);
            return "visits/userVisits";
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @GetMapping("/visit-form")
    public String getVisitForm(@RequestParam Long tgUserId, Model model) {
        model.addAttribute("services", userVisitBotService.findAllServices());
        model.addAttribute("tgUserId", tgUserId);
        return "visits/createVisitForUser";
    }

}
