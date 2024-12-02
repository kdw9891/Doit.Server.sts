package com.uni.doit.framework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JsonUtils {

    private final ObjectMapper objectMapper;

    public JsonUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // ObjectNode 생성 메서드
    public ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    // Map을 ObjectNode로 변환하는 메서드
    public ObjectNode convertMapToJsonObject(Map<String, Object> map) {
        ObjectNode jsonObject = createObjectNode();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();

            // LocalDateTime 처리
            if (value instanceof LocalDateTime) {
                jsonObject.put(entry.getKey(), DateUtils.formatDateTime(value)); // LocalDateTime 포맷
            } else if (value instanceof LocalDate) {
                jsonObject.put(entry.getKey(), DateUtils.formatDate(value)); // LocalDate 포맷
            } else if (value instanceof Date) {
                jsonObject.put(entry.getKey(), DateUtils.formatDateTime(value)); // Date 포맷
            } else if (value instanceof Number) {
                jsonObject.put(entry.getKey(), ((Number) value).toString()); // 숫자 처리
            } else if (value instanceof Boolean) {
                jsonObject.put(entry.getKey(), (Boolean) value); // Boolean 처리
            } else if (value != null) {
                jsonObject.put(entry.getKey(), value.toString()); // 기본 값 처리
            } else {
                jsonObject.putNull(entry.getKey()); // null 처리
            }
        }
        return jsonObject;
    }

    // List<Map<String, Object>>를 ArrayNode로 변환하는 메서드
    public ArrayNode convertListToJsonArray(List<Map<String, Object>> list) {
        ArrayNode jsonArray = objectMapper.createArrayNode();
        for (Map<String, Object> map : list) {
            jsonArray.add(convertMapToJsonObject(map)); // convertMapToJsonObject 활용
        }
        return jsonArray;
    }
}
