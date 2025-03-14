package ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChooseCancelVisitCallbackData extends BaseCallbackData {

    @JsonProperty("v_id")
    protected Long visitId;

    public ChooseCancelVisitCallbackData() {
    }

    public Long getVisitId() {
        return visitId;
    }

    public void setVisitId(Long visitId) {
        this.visitId = visitId;
    }
}
