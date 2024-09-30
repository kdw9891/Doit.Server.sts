package com.uni.doit.framework.utils;

import java.util.HashMap;
import java.util.Map;

public class ParamUtils {

    // 가변 인자를 사용하여 바로 Map<String, Object>를 생성하는 메서드
    public static Map<String, Object> createParams(String... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("키와 값의 개수가 일치하지 않습니다.");
        }

        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            String key = keyValuePairs[i];
            String value = keyValuePairs[i + 1];
            params.put(key, value);
        }
        return params;
    }
}