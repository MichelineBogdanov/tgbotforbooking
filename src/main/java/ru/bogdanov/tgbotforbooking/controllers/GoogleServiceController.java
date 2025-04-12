package ru.bogdanov.tgbotforbooking.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/webhook/calendar")
public class GoogleServiceController {

    private static final Logger log = LoggerFactory.getLogger(GoogleServiceController.class);

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
