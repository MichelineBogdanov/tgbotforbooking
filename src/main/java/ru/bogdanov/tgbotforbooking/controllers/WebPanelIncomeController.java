package ru.bogdanov.tgbotforbooking.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bogdanov.tgbotforbooking.services.bot_services.UserVisitBotService;

import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/income")
public class WebPanelIncomeController extends AbstractWebPanelController {

    public WebPanelIncomeController(UserVisitBotService userVisitBotService) {
        super(userVisitBotService);
    }

    @GetMapping
    public String getIncomeData(
            @RequestParam(value = "month", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            Model model) {
        try {
            if (month == null) {
                month = YearMonth.now();
            }
            List<Long> dailyIncome = userVisitBotService.getIncomeForMonthByDays(month);
            long totalIncome = dailyIncome.stream().mapToLong(Long::longValue).sum();
            model.addAttribute("totalIncome", totalIncome);
            model.addAttribute("dailyIncome", dailyIncome);
            model.addAttribute("currentMonth", month);
            return "income/income";
        } catch (Exception e) {
            return null;
        }
    }
}
