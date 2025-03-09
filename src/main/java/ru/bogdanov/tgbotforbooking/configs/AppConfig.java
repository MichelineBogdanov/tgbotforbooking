package ru.bogdanov.tgbotforbooking.configs;

import com.google.api.services.calendar.Calendar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.bogdanov.tgbotforbooking.servises.google.GoogleServiceFactory;

@Configuration
@PropertySource("classpath:application.properties")
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
