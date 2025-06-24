package ru.bogdanov.tgbotforbooking.services.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.bogdanov.tgbotforbooking.services.telegram.callbacks.CallbackTypes;

public class JsonHandler {

    private final static ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T readToObject(String value, Class<T> clazz) {
        try {
            return mapper.readValue(value, clazz);
        } catch (ClassCastException | JsonProcessingException e) {
            return null;
        }
    }

    public static CallbackTypes getType(String json) {
        try {
            JsonNode jsonNode = mapper.readTree(json);
            return CallbackTypes.fromValue(jsonNode.get("type").asInt());
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
