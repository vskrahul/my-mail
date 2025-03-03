package com.github.vskrahul.azure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> String toJsonString(T instance) {
        String json = null;
        try {
            json = mapper.writeValueAsString(instance);
            log.debug("[method=toJson] [json={}]", json);
        } catch (JsonProcessingException e) {
            log.error("[method=toJson] [JsonProcessingException={}]", e.toString());
        }
        return json;
    }

    public static <T> String toPrettyJsonString(T instance) {
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(instance);
            log.debug("[method=toJson] [json={}]", json);
        } catch (JsonProcessingException e) {
            log.error("[method=toJson] [JsonProcessingException={}]", e);
        }
        return json;
    }

    public static String toIntendedJson(String json) {
        String indentedJson = null;
        if(Objects.isNull(json) || "".equals(json.trim()))
            return null;
        try {
            indentedJson = mapper.writeValueAsString(mapper.readValue(json, Object.class));
            log.debug("[method=toJson] [json={}]", json);
        } catch (Exception e) {
            log.error("[method=toJson] [JsonProcessingException={}]", e);
            indentedJson = json;
        }
        return indentedJson;
    }

    public static <T> T jsonStringToInstance(String json, Class<T> type) {
        T t = null;
        try {
            t = mapper.readValue(json, type);
            log.debug("[method=jsonStringToInstance] [json={}]", json);
        } catch (IOException e) {
            log.error("[method=jsonStringToInstance] [IOException={}]", e);
        }
        return t;
    }
}
