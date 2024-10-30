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

/**
 * 요청 파라미터를 처리하고 검증하는 유틸리티 클래스
 * JSON 문자열과 DTO 객체로부터 파라미터를 검증하는 메서드들을 제공합니다.
 */
@Component
public class RequestUtils {
    
    /** JSON 처리를 위한 ObjectMapper 인스턴스 */
    private final ObjectMapper objectMapper;

    /**
     * ObjectMapper를 초기화하는 생성자
     * @param objectMapper JSON 처리를 위한 Jackson ObjectMapper
     */
    public RequestUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * JSON 문자열에서 필수 파라미터들을 검증하는 메서드
     * 모든 필수 파라미터가 존재하는지 확인하고 적절한 데이터 타입으로 변환합니다.
     *
     * @param jsonString 검증할 JSON 문자열
     * @param requiredParams 필수 파라미터 이름들의 가변 배열
     * @return 검증된 파라미터들이나 오류 메시지를 포함하는 ResponseEntity
     * @throws IOException JSON 파싱 실패시 발생
     */
    public ResponseEntity<Map<String, Object>> validateJsonParams(String jsonString, String... requiredParams) throws IOException {
        // 결과를 저장할 맵 객체 생성
        Map<String, Object> param = new HashMap<>();
        // 누락된 파라미터들을 저장할 StringBuilder
        StringBuilder missingParams = new StringBuilder();
        
        // JSON 문자열을 JsonNode 객체로 파싱
        JsonNode jsonObj = objectMapper.readTree(jsonString);

        // 각 필수 파라미터 검사
        for (String paramKey : requiredParams) {
            JsonNode valueNode = jsonObj.get(paramKey);
            
            // 파라미터가 없거나 null인 경우 처리
            if (valueNode == null || valueNode.isNull()) {
                if (missingParams.length() > 0) {
                    missingParams.append(", ");
                }
                missingParams.append("'").append(paramKey).append("'");
            } else {
                // 다양한 데이터 타입 처리
                if (valueNode.isInt()) {
                    param.put(paramKey, valueNode.intValue());        // 정수형
                } else if (valueNode.isBoolean()) {
                    param.put(paramKey, valueNode.booleanValue());    // 불리언
                } else if (valueNode.isDouble()) {
                    param.put(paramKey, valueNode.doubleValue());     // 실수형
                } else {
                    param.put(paramKey, valueNode.asText());          // 문자열
                }
            }
        }

        // 누락된 파라미터가 있는 경우 오류 응답 반환
        if (missingParams.length() > 0) {
            String errorMessage = String.format("Parameters %s are missing.", missingParams.toString());
            Map<String, Object> errorObj = new HashMap<>();
            errorObj.put("error", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObj);
        }
        
        // 모든 검증 통과시 성공 응답 반환
        return ResponseEntity.ok(param);
    }

    /**
     * DTO 객체에서 필수 파라미터들을 검증하는 메서드
     * 리플렉션을 사용하여 DTO의 필드값들을 검사합니다.
     *
     * @param dto 검증할 DTO 객체
     * @param requiredParams 필수 파라미터 이름들의 가변 배열
     * @return 검증된 파라미터들이나 오류 메시지를 포함하는 ResponseEntity
     */
    public <T> ResponseEntity<Map<String, Object>> validateDtoParams(T dto, String... requiredParams) {
        // 결과를 저장할 맵 객체 생성
        Map<String, Object> param = new HashMap<>();
        // 누락된 파라미터들을 저장할 StringBuilder
        StringBuilder missingParams = new StringBuilder();

        // 각 필수 파라미터 검사
        for (String paramKey : requiredParams) {
            try {
                // 리플렉션으로 DTO의 필드 접근
                Field field = dto.getClass().getDeclaredField(paramKey);
                field.setAccessible(true);
                Object value = field.get(dto);

                // 필드값이 null이거나 빈 문자열인 경우 처리
                if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                    if (missingParams.length() > 0) {
                        missingParams.append(", ");
                    }
                    missingParams.append("'").append(paramKey).append("'");
                } else {
                    param.put(paramKey, value);
                }
            } catch (NoSuchFieldException e) {
                // DTO에 해당 필드가 없는 경우
                throw new IllegalArgumentException("필드 '" + paramKey + "'가 DTO에 존재하지 않습니다.");
            } catch (IllegalAccessException e) {
                // 필드 접근 실패시
                throw new RuntimeException("필드 '" + paramKey + "' 접근에 실패했습니다.", e);
            }
        }

        // 누락된 파라미터가 있는 경우 오류 응답 반환
        if (missingParams.length() > 0) {
            String errorMessage = String.format("Parameters %s are missing.", missingParams.toString());
            Map<String, Object> errorObj = new HashMap<>();
            errorObj.put("error", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObj);
        }

        // 모든 검증 통과시 성공 응답 반환
        return ResponseEntity.ok(param);
    }
}