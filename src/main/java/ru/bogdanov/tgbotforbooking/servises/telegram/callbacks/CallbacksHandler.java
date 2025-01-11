package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.servises.telegram.JsonHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.BaseCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.ChooseTimeCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.CreateVisitCallbackData;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info.PlaceInfoCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info.ScheduleInfoCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.CancelVisitCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit.ChooseDayCallBack;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit.ChooseTimeCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit.CreateVisitCallback;

import java.util.Map;

@Component
public class CallbacksHandler {

    private final Map<CallbackTypes, CallbackHandler> handler;

    public CallbacksHandler(PlaceInfoCallback placeInfoCallback
            , ScheduleInfoCallback scheduleInfoCallback
            , ChooseDayCallBack chooseDayCallBack
            , ChooseTimeCallback chooseTimeCallback
            , CreateVisitCallback createVisitCallback
            , CancelVisitCallback cancelVisitCallback) {
        this.handler = Map.of(CallbackTypes.GET_PLACE_INFO, placeInfoCallback
                , CallbackTypes.GET_SCHEDULE, scheduleInfoCallback
                , CallbackTypes.CHOOSE_DAY, chooseDayCallBack
                , CallbackTypes.CHOOSE_TIME, chooseTimeCallback
                , CallbackTypes.CREATE_VISIT, createVisitCallback
                , CallbackTypes.CANCEL_VISIT, cancelVisitCallback);
    }

    public SendMessage handleCallbacks(Update update) {
        BaseCallbackData callBackData = getCallbackData(update);
        CallbackHandler callbackBiFunction = handler.get(callBackData.getType());
        return callbackBiFunction.apply(callBackData, update);
    }

    private BaseCallbackData getCallbackData(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        CallbackTypes type = JsonHandler.getType(callbackData);
        BaseCallbackData result = null;
        switch (type) {
            case CHOOSE_DAY, GET_SCHEDULE -> result = JsonHandler.readToObject(callbackData, BaseCallbackData.class);
            case CHOOSE_TIME -> result = JsonHandler.readToObject(callbackData, ChooseTimeCallbackData.class);
            case CREATE_VISIT -> result = JsonHandler.readToObject(callbackData, CreateVisitCallbackData.class);
        }
        return result;
    }

}
