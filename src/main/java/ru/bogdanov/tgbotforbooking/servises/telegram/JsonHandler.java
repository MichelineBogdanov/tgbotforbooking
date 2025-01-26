package ru.bogdanov.tgbotforbooking.servises.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.bogdanov.tgbotforbooking.servises.telegram.callbacks.CallbackTypes;

import java.util.List;


public class JsonHandler {
    private final static ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(Object object, Class<T> clazz) {
        try {
            return mapper.convertValue(object, clazz);
        } catch (ClassCastException e) {
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
            return CallbackTypes.valueOf(String.valueOf(jsonNode.get("type").asText()));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static List<String> toList(String json) {
        try {
            return mapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            return List.of();
        }
    }
}
