package ru.bogdanov.tgbotforbooking.servises.google;

public record CreateVisitResult(String resultMessage) {

    @Override
    public String toString() {
        return "CreateVisitResult{" +
                "resultMessage='" + resultMessage + '\'' +
                '}';
    }
}
