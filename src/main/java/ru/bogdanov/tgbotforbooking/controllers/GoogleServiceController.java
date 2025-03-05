package ru.bogdanov.tgbotforbooking.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/webhook/calendar")
@Slf4j
public class GoogleServiceController {

    @PostMapping("/webhook/calendar")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader("X-Goog-Resource-State") String resourceState) {
        if ("not_exists".equals(resourceState)) {
            // Обработка удаления события
            log.info("!!!!!!!!!!!!!!!!!!" + payload);
            System.out.println("Event deleted: " + payload);
        }
        return ResponseEntity.ok("Webhook received");
    }

}
