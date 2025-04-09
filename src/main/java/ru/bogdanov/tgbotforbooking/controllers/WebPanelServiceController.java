package ru.bogdanov.tgbotforbooking.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bogdanov.tgbotforbooking.entities.CosmetologyService;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;

@Controller
@RequestMapping("/services")
public class WebPanelServiceController extends AbstractWebPanelController {

    public WebPanelServiceController(UserVisitBotService userVisitBotService) {
        super(userVisitBotService);
    }

    @GetMapping
    public String getServices(Model model) {
        model.addAttribute("services", userVisitBotService.findAllServices());
        return "services/services";
    }

    @PostMapping("/update")
    public ResponseEntity<CosmetologyService> updateService(@RequestBody CosmetologyService service) {
        try {
            CosmetologyService savedService = userVisitBotService.updateService(service);
            return ResponseEntity.ok(savedService);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<String> deleteService(@PathVariable Long serviceId) {
        try {
            userVisitBotService.deleteService(serviceId);
            return ResponseEntity.ok("Вы успешно удалили услугу!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при удалении услуги: " + e.getMessage());
        }
    }

}
