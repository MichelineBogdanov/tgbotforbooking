package ru.bogdanov.tgbotforbooking;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class TgBotForBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgBotForBookingApplication.class, args);
	}

}
