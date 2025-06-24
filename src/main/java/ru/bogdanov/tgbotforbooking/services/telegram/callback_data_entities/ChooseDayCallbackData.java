package ru.bogdanov.tgbotforbooking.services.telegram.callback_data_entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChooseDayCallbackData extends BaseCallbackData {

    @JsonProperty("s_id")
    protected Long serviceId;

    public ChooseDayCallbackData() {
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}
