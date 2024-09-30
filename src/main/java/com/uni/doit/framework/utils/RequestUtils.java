package com.uni.doit.framework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestUtils {

    private final ObjectMapper objectMapper;

    public RequestUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // JSON String 기반 파라미터 유효성 검사 메서드
    public ResponseEntity<Map<String, Object>> validateJsonParams(String jsonString, String... requiredParams) throws IOException {
        Map<String, Object> param = new HashMap<>();
        StringBuilder missingParams = new StringBuilder();
        JsonNode jsonObj = objectMapper.readTree(jsonString);

        for (String paramKey : requiredParams) {
            JsonNode valueNode = jsonObj.get(paramKey);
            if (valueNode == null || valueNode.isNull()) {
                if (missingParams.length() > 0) {
                    missingParams.append(", ");
                }
                missingParams.append("'").append(paramKey).append("'");
            } else {
                param.put(paramKey, valueNode.asText());
            }
        }

        if (missingParams.length() > 0) {
            String errorMessage = String.format("Parameters %s are missing.", missingParams.toString());
            Map<String, Object> errorObj = new HashMap<>();
            errorObj.put("error", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObj);
        }

        return ResponseEntity.ok(param);
    }

    // DTO 기반 파라미터 유효성 검사 메서드
    public <T> ResponseEntity<Map<String, Object>> validateDtoParams(T dto, String... requiredParams) {
        Map<String, Object> param = new HashMap<>();
        StringBuilder missingParams = new StringBuilder();

        for (String paramKey : requiredParams) {
            try {
                Field field = dto.getClass().getDeclaredField(paramKey);
                field.setAccessible(true);
                Object value = field.get(dto);

                if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                    if (missingParams.length() > 0) {
                        missingParams.append(", ");
                    }
                    missingParams.append("'").append(paramKey).append("'");
                } else {
                    param.put(paramKey, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                if (missingParams.length() > 0) {
                    missingParams.append(", ");
                }
                missingParams.append("'").append(paramKey).append("'");
            }
        }

        if (missingParams.length() > 0) {
            String errorMessage = String.format("Parameters %s are missing.", missingParams.toString());
            Map<String, Object> errorObj = new HashMap<>();
            errorObj.put("error", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObj);
        }

        return ResponseEntity.ok(param);
    }
}
