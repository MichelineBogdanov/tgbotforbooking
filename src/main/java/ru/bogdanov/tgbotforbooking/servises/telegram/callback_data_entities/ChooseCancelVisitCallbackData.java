package ru.bogdanov.tgbotforbooking.servises.telegram.callback_data_entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChooseCancelVisitCallbackData extends BaseCallbackData {

    @JsonProperty("visitId")
    protected String visitId;

    public ChooseCancelVisitCallbackData() {
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }
}
