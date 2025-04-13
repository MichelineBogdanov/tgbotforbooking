package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bogdanov.tgbotforbooking.entities.CosmetologyService;
import ru.bogdanov.tgbotforbooking.servises.bot_services.UserVisitBotService;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.commands.CommandTypes;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.KeyboardBuilder;
import ru.bogdanov.tgbotforbooking.servises.telegram.utils.MessagesText;

import java.util.List;

@Component
public class ServicesInfoCallback implements CallbackHandler {

    private final UserVisitBotService userVisitBotService;

    public ServicesInfoCallback(UserVisitBotService userVisitBotService) {
        this.userVisitBotService = userVisitBotService;
    }

    @Override
    public SendMessage apply(BaseCallbackData callback, Update update) {
        List<CosmetologyService> allServices = userVisitBotService.findAllServices();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        String messageText;
        if (allServices.isEmpty()) {
            messageText = MessagesText.NO_SERVICES_YET_INFO_TEXT;
        } else {
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < allServices.size(); i++) {
                CosmetologyService service = allServices.get(i);
                text.append("<b>")
                        .append(i + 1)
                        .append(" - ")
                        .append(service.getName())
                        .append("\n")
                        .append("Цена : ")
                        .append(service.getPrice())
                        .append("р.")
                        .append("\n")
                        .append("Длительность : ")
                        .append(service.getDuration())
                        .append(" мин.</b>")
                        .append("\n")
                        .append("<i>")
                        .append(service.getDescription())
                        .append("</i>")
                        .append("\n");
            }
            messageText = String.format(MessagesText.GET_SERVICES_INFO_TEXT, text);
            message.setParseMode(ParseMode.HTML);
        }
        message.setText(messageText);
        InlineKeyboardMarkup keyboardMarkup = new KeyboardBuilder().addBackButton(CommandTypes.GENERAL_INFO).build();
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

}
