package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.servises.telegram.JsonHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.*;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.common.BackCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info.GetVisitsCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info.PlaceInfoCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info.ScheduleInfoCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info.UserNotificationSwitchCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.cancel_visit.CancelVisitCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.cancel_visit.ChooseCancelVisitCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit.ChooseDayCallBack;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit.ChooseServiceCallBack;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit.ChooseTimeCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit.CreateVisitCallback;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class CallbacksHandler {

    private final Map<CallbackTypes, CallbackHandler> handler;

    public CallbacksHandler(GetVisitsCallback getVisitsCallback
            , PlaceInfoCallback placeInfoCallback
            , ScheduleInfoCallback scheduleInfoCallback
            , UserNotificationSwitchCallback userNotificationSwitchCallback
            , ChooseDayCallBack chooseDayCallBack
            , ChooseTimeCallback chooseTimeCallback
            , CreateVisitCallback createVisitCallback
            , ChooseServiceCallBack chooseServiceCallBack
            , ChooseCancelVisitCallback chooseCancelVisitCallback
            , CancelVisitCallback cancelVisitCallback
            , BackCallback backCallback) {
        this.handler = Collections.unmodifiableMap(new HashMap<>() {{
            put(CallbackTypes.GET_VISITS, getVisitsCallback);
            put(CallbackTypes.GET_PLACE_INFO, placeInfoCallback);
            put(CallbackTypes.GET_SCHEDULE, scheduleInfoCallback);
            put(CallbackTypes.NOTIFICATIONS_SWITCH, userNotificationSwitchCallback);
            put(CallbackTypes.CHOOSE_DAY, chooseDayCallBack);
            put(CallbackTypes.CHOOSE_TIME, chooseTimeCallback);
            put(CallbackTypes.CHOOSE_SERVICE, chooseServiceCallBack);
            put(CallbackTypes.CREATE_VISIT, createVisitCallback);
            put(CallbackTypes.CHOOSE_CANCEL_VISIT, chooseCancelVisitCallback);
            put(CallbackTypes.CANCEL_VISIT, cancelVisitCallback);
            put(CallbackTypes.BACK, backCallback);
        }});
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
        switch (Objects.requireNonNull(type)) {
            case GET_VISITS, GET_PLACE_INFO, GET_SCHEDULE, CHOOSE_DAY, CANCEL_VISIT, NOTIFICATIONS_SWITCH ->
                    result = JsonHandler.readToObject(callbackData, BaseCallbackData.class);
            case CHOOSE_TIME -> result = JsonHandler.readToObject(callbackData, ChooseTimeCallbackData.class);
            case CHOOSE_SERVICE -> result = JsonHandler.readToObject(callbackData, ChooseServiceCallbackData.class);
            case CREATE_VISIT -> result = JsonHandler.readToObject(callbackData, CreateVisitCallbackData.class);
            case CHOOSE_CANCEL_VISIT ->
                    result = JsonHandler.readToObject(callbackData, ChooseCancelVisitCallbackData.class);
            case BACK -> result = JsonHandler.readToObject(callbackData, BackCallBackData.class);
        }
        return result;
    }

}
