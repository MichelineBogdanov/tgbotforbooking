package ru.bogdanov.tgbotforbooking.configs;

import com.google.api.services.calendar.Calendar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.bogdanov.tgbotforbooking.serviсes.google.GoogleServiceFactory;

@Configuration
@PropertySource("classpath:application.yaml")
@EnableScheduling
public class AppConfig {

    private final GoogleServiceFactory googleServiceFactory;

    public AppConfig(GoogleServiceFactory googleServiceFactory) {
        this.googleServiceFactory = googleServiceFactory;
    }

    @Bean(name = "calendarService")
    public Calendar createCalendarService() {
        return googleServiceFactory.getCalendarService();
    }

}
