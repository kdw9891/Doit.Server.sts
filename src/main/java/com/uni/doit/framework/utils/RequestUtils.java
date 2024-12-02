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
 * 요청 파라미터 처리 및 검증을 위한 유틸리티 클래스
 * 
 * 이 클래스의 주요 기능:
 * 1. JSON 문자열에서 필수 파라미터 검증
 * 2. DTO 객체의 필수 파라미터 검증
 * 3. 다양한 데이터 타입 처리
 * 4. 누락된 파라미터에 대한 오류 응답 생성
 * 
 * 주요 메서드:
 * - validateJsonParams(): JSON 문자열 파라미터 검증
 * - validateDtoParams(): DTO 객체 파라미터 검증
 */
@Component
public class RequestUtils {
    
    /** 
     * JSON 데이터 처리를 위한 Jackson ObjectMapper
     * - JSON 문자열을 객체로 변환
     * - JSON 노드 파싱 지원
     */
    private final ObjectMapper objectMapper;

    /**
     * ObjectMapper 의존성 주입을 위한 생성자
     * 
     * @param objectMapper Spring Bean으로 주입되는 ObjectMapper 인스턴스
     */
    public RequestUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * JSON 문자열의 필수 파라미터를 검증하는 메서드
     * 
     * 주요 동작:
     * 1. JSON 문자열 파싱
     * 2. 필수 파라미터 존재 여부 확인
     * 3. 데이터 타입에 따른 값 변환
     * 4. 누락된 파라미터 존재 시 오류 응답 생성
     * 
     * 지원되는 데이터 타입:
     * - 정수(int)
     * - 부울(boolean)
     * - 실수(double)
     * - 문자열(String)
     * 
     * @param jsonString 검증할 JSON 문자열
     * @param requiredParams 필수로 확인할 파라미터 이름들
     * @return 검증 결과 ResponseEntity (성공 시 파라미터 맵, 실패 시 오류 메시지)
     * @throws IOException JSON 파싱 중 발생할 수 있는 예외
     */
    public ResponseEntity<Map<String, Object>> validateJsonParams(String jsonString, String... requiredParams) throws IOException {
        // 검증된 파라미터를 저장할 맵 생성
        Map<String, Object> param = new HashMap<>();
        
        // 누락된 파라미터를 추적할 StringBuilder
        StringBuilder missingParams = new StringBuilder();
        
        // JSON 문자열을 JsonNode 객체로 변환
        JsonNode jsonObj = objectMapper.readTree(jsonString);

        // 각 필수 파라미터에 대해 반복 검사
        for (String paramKey : requiredParams) {
            JsonNode valueNode = jsonObj.get(paramKey);
            
            // 파라미터 누락 또는 null 값 확인
            if (valueNode == null || valueNode.isNull()) {
                // 누락된 파라미터 목록에 추가 (쉼표로 구분)
                if (missingParams.length() > 0) {
                    missingParams.append(", ");
                }
                missingParams.append("'").append(paramKey).append("'");
            } else {
                // 데이터 타입에 따른 값 변환 및 저장
                if (valueNode.isInt()) {
                    param.put(paramKey, valueNode.intValue());        // 정수형 처리
                } else if (valueNode.isBoolean()) {
                    param.put(paramKey, valueNode.booleanValue());    // 불리언 처리
                } else if (valueNode.isDouble()) {
                    param.put(paramKey, valueNode.doubleValue());     // 실수형 처리
                } else {
                    param.put(paramKey, valueNode.asText());          // 문자열 처리
                }
            }
        }

        // 누락된 파라미터가 있는 경우 오류 응답 생성
        if (missingParams.length() > 0) {
            String errorMessage = String.format("Parameters %s are missing.", missingParams.toString());
            Map<String, Object> errorObj = new HashMap<>();
            errorObj.put("error", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObj);
        }
        
        // 모든 파라미터 검증 성공 시 OK 응답 반환
        return ResponseEntity.ok(param);
    }

    /**
     * DTO 객체의 필수 파라미터를 검증하는 메서드
     * 
     * 주요 특징:
     * 1. 자바 리플렉션 API 사용
     * 2. DTO 객체의 필드 값 동적 검사
     * 3. 누락된 필드나 빈 문자열 탐지
     * 
     * 오류 처리:
     * - 필드 미존재: IllegalArgumentException
     * - 필드 접근 실패: RuntimeException
     * 
     * @param dto 검증할 DTO 객체 (제네릭 타입)
     * @param requiredParams 필수로 확인할 파라미터 이름들
     * @return 검증 결과 ResponseEntity (성공 시 파라미터 맵, 실패 시 오류 메시지)
     */
    public <T> ResponseEntity<Map<String, Object>> validateDtoParams(T dto, String... requiredParams) {
        // 검증된 파라미터를 저장할 맵 생성
        Map<String, Object> param = new HashMap<>();
        
        // 누락된 파라미터를 추적할 StringBuilder
        StringBuilder missingParams = new StringBuilder();

        // 각 필수 파라미터에 대해 반복 검사
        for (String paramKey : requiredParams) {
            try {
                // 리플렉션을 통해 DTO 클래스의 특정 필드 접근
                Field field = dto.getClass().getDeclaredField(paramKey);
                field.setAccessible(true);  // private 필드 접근 허용
                Object value = field.get(dto);

                // null 값 또는 빈 문자열 확인
                if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                    // 누락된 파라미터 목록에 추가 (쉼표로 구분)
                    if (missingParams.length() > 0) {
                        missingParams.append(", ");
                    }
                    missingParams.append("'").append(paramKey).append("'");
                } else {
                    param.put(paramKey, value);
                }
            } catch (NoSuchFieldException e) {
                // DTO에 해당 필드가 없는 경우 예외 처리
                throw new IllegalArgumentException("필드 '" + paramKey + "'가 DTO에 존재하지 않습니다.");
            } catch (IllegalAccessException e) {
                // 필드 접근 실패 시 예외 처리
                throw new RuntimeException("필드 '" + paramKey + "' 접근에 실패했습니다.", e);
            }
        }

        // 누락된 파라미터가 있는 경우 오류 응답 생성
        if (missingParams.length() > 0) {
            String errorMessage = String.format("Parameters %s are missing.", missingParams.toString());
            Map<String, Object> errorObj = new HashMap<>();
            errorObj.put("error", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObj);
        }

        // 모든 파라미터 검증 성공 시 OK 응답 반환
        return ResponseEntity.ok(param);
    }
}