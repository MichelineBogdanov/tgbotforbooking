package ru.bogdanov.tgbotforbooking.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateVisitDTO {

    private LocalDate date;
    private LocalTime time;
    private Long tgUserId;
    private Long serviceId;

    public CreateVisitDTO() {
    }

    public CreateVisitDTO(LocalDate date, LocalTime time, Long tgUserId, Long serviceId) {
        this.date = date;
        this.time = time;
        this.tgUserId = tgUserId;
        this.serviceId = serviceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Long getTgUserId() {
        return tgUserId;
    }

    public void setTgUserId(Long tgUserId) {
        this.tgUserId = tgUserId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

}
