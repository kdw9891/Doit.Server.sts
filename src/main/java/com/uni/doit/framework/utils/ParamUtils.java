package com.uni.doit.framework.utils;

import java.util.HashMap;
import java.util.Map;

public class ParamUtils {

    public static Map<String, Object> createParams(Object... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("키와 값의 개수가 일치하지 않습니다.");
        }

        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            if (!(keyValuePairs[i] instanceof String)) {
                throw new IllegalArgumentException("키는 문자열이어야 합니다: index " + i);
            }
            String key = (String) keyValuePairs[i];
            Object value = keyValuePairs[i + 1];
            params.put(key, value);
        }
        return params;
    }
}
