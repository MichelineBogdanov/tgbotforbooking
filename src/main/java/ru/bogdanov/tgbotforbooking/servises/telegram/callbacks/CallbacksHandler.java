package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.servises.telegram.JsonHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info.PlaceInfoCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info.ScheduleInfoCallback;

import java.util.List;
import java.util.Map;

@Component
public class CallbacksHandler {

    private final Map<CallbackTypes, CallbackHandler> handler;

    public CallbacksHandler(PlaceInfoCallback placeInfoCallback
            , ScheduleInfoCallback scheduleInfoCallback) {
        this.handler = Map.of(CallbackTypes.GET_PLACE_INFO, placeInfoCallback
                , CallbackTypes.GET_SCHEDULE, scheduleInfoCallback);
    }

    public SendMessage handleCallbacks(Update update) {
        List<String> list = JsonHandler.toList(update.getCallbackQuery().getData());
        SendMessage answer;
        Callback callback = new Callback(CallbackTypes.valueOf(list.get(0)));
        CallbackHandler callbackBiFunction = handler.get(callback.getCallbackType());
        answer = callbackBiFunction.apply(callback, update);
        return answer;
    }

}
