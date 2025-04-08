package ru.bogdanov.tgbotforbooking.servises.telegram.callbacks;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bogdanov.tgbotforbooking.servises.telegram.JsonHandler;
import ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities.*;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.account.GetFutureVisitsCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.account.GetVisitsHistoryCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.account.UserNotificationSwitchCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.common.BackCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info.PlaceInfoCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info.ScheduleInfoCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.general_info.ServicesInfoCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.cancel_visit.CancelVisitCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.cancel_visit.ChooseCancelVisitCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit.ChooseDayCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit.ChooseServiceCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit.ChooseTimeCallback;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.visit_deals.create_visit.CreateVisitCallback;

import java.util.Map;
import java.util.Objects;

@Component
public class CallbacksHandler {

    private final Map<CallbackTypes, CallbackHandler> handler;

    public CallbacksHandler(GetFutureVisitsCallback getFutureVisitsCallback
            , GetVisitsHistoryCallback getVisitsHistoryCallback
            , PlaceInfoCallback placeInfoCallback
            , ScheduleInfoCallback scheduleInfoCallback
            , ServicesInfoCallback servicesInfoCallback
            , UserNotificationSwitchCallback userNotificationSwitchCallback
            , ChooseDayCallback chooseDayCallback
            , ChooseTimeCallback chooseTimeCallback
            , CreateVisitCallback createVisitCallback
            , ChooseServiceCallback chooseServiceCallback
            , ChooseCancelVisitCallback chooseCancelVisitCallback
            , CancelVisitCallback cancelVisitCallback
            , BackCallback backCallback) {
        this.handler = Map.ofEntries(Map.entry(CallbackTypes.GET_FUTURE_VISITS, getFutureVisitsCallback)
                , Map.entry(CallbackTypes.GET_VISITS_HISTORY, getVisitsHistoryCallback)
                , Map.entry(CallbackTypes.GET_PLACE_INFO, placeInfoCallback)
                , Map.entry(CallbackTypes.GET_SCHEDULE, scheduleInfoCallback)
                , Map.entry(CallbackTypes.GET_SERVICES_LIST_INFO, servicesInfoCallback)
                , Map.entry(CallbackTypes.NOTIFICATIONS_SWITCH, userNotificationSwitchCallback)
                , Map.entry(CallbackTypes.CHOOSE_DAY, chooseDayCallback)
                , Map.entry(CallbackTypes.CHOOSE_TIME, chooseTimeCallback)
                , Map.entry(CallbackTypes.CHOOSE_SERVICE, chooseServiceCallback)
                , Map.entry(CallbackTypes.CREATE_VISIT, createVisitCallback)
                , Map.entry(CallbackTypes.CHOOSE_CANCEL_VISIT, chooseCancelVisitCallback)
                , Map.entry(CallbackTypes.CANCEL_VISIT, cancelVisitCallback)
                , Map.entry(CallbackTypes.BACK, backCallback));
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
            case GET_FUTURE_VISITS
                    , GET_VISITS_HISTORY
                    , GET_PLACE_INFO
                    , GET_SCHEDULE
                    , GET_SERVICES_LIST_INFO
                    , CHOOSE_SERVICE
                    , CANCEL_VISIT
                    , NOTIFICATIONS_SWITCH -> result = JsonHandler.readToObject(callbackData, BaseCallbackData.class);
            case CHOOSE_TIME -> result = JsonHandler.readToObject(callbackData, ChooseTimeCallbackData.class);
            case CHOOSE_DAY -> result = JsonHandler.readToObject(callbackData, ChooseDayCallbackData.class);
            case CREATE_VISIT -> result = JsonHandler.readToObject(callbackData, CreateVisitCallbackData.class);
            case CHOOSE_CANCEL_VISIT ->
                    result = JsonHandler.readToObject(callbackData, ChooseCancelVisitCallbackData.class);
            case BACK -> result = JsonHandler.readToObject(callbackData, BackCallbackData.class);
        }
        return result;
    }

}
