package ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateVisitCallbackData extends ChooseServiceCallbackData {

    @JsonProperty("s_id")
    protected Long serviceId;

    public CreateVisitCallbackData() {
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}
