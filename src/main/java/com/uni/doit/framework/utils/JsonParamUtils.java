package com.uni.doit.framework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JsonParamUtils {

    private final ObjectMapper objectMapper;

    public JsonParamUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 기본값이 없는 경우의 파라미터 맵을 JSON 객체에서 생성합니다.
     * @param jsonString JSON 문자열
     * @param keys 파라미터로 사용할 키들의 배열
     * @return 파라미터 맵
     */
    public Map<String, Object> getParams(String jsonString, String... keys) throws IOException {
        Map<String, Object> params = new HashMap<>();
        JsonNode jsonObj = objectMapper.readTree(jsonString);

        for (String key : keys) {
            JsonNode valueNode = jsonObj.get(key);
            params.put(key, Optional.ofNullable(valueNode)
                    .filter(node -> !node.isNull())
                    .map(JsonNode::asText)
                    .orElse("")); // 기본값 설정
        }

        return params;
    }
}
