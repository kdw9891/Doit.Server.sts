package com.uni.doit.framework.utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JSON 응답 생성을 도와주는 유틸리티 클래스.
 * MyBatis를 사용하여 쿼리를 실행하고, 결과를 JSON 형식으로 변환하여 반환.
 */
@Component
public class JsonResultUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonResultUtils.class); // 로깅을 위한 Logger 설정
    private final JsonUtils jsonUtils; // JSON 처리를 위한 JsonUtils 객체

    /**
     * JsonResultUtils 생성자
     * @param jsonUtils JSON 처리를 담당하는 유틸리티 클래스 객체
     */
    public JsonResultUtils(JsonUtils jsonUtils) {
        this.jsonUtils = jsonUtils;
    }

    /**
     * MyBatis 쿼리를 실행하고 결과를 JSON으로 변환하여 반환 (배열 키 지정 가능).
     * @param session MyBatis SqlSession 객체
     * @param statement 실행할 MyBatis 쿼리 ID
     * @param param 쿼리에 전달할 매개변수
     * @param arrayKey JSON 응답에서 배열로 반환될 키 이름
     * @return 결과를 포함한 JSON 객체
     */
    public ObjectNode getJsonResult(SqlSession session, String statement, Map<String, Object> param, String arrayKey) {
        ObjectNode jsonObject = jsonUtils.createObjectNode(); // 빈 JSON 객체 생성

        try {
            if (session == null) { // SqlSession이 null인 경우 예외 처리
                throw new IllegalArgumentException("SqlSession is null");
            }

            logger.info("Executing MyBatis query: {}", statement);
            List<Map<String, Object>> result = session.selectList(statement, param); // MyBatis 쿼리 실행

            // 결과 처리
            if (Optional.ofNullable(result).orElse(List.of()).isEmpty()) {
                // 결과가 없는 경우
                logger.info("Query returned no results");
                jsonObject.put("RSLT_CD", "01"); // 실패 코드
            } else {
                // 결과가 있는 경우
                logger.info("Query returned {} results", result.size());
                ArrayNode jsonArray = jsonUtils.convertListToJsonArray(result); // 결과를 JSON 배열로 변환
                jsonObject.put("RSLT_CD", "00"); // 성공 코드
                jsonObject.set(arrayKey, jsonArray); // 배열 데이터를 지정된 키로 추가
            }
        } catch (Exception e) {
            // 쿼리 실행 중 예외 발생
            logger.error("Error executing query: {}", e.getMessage(), e);
            jsonObject.put("RSLT_CD", "99"); // 에러 코드
            jsonObject.put("ERROR_MSG", e.getMessage()); // 에러 메시지 추가
        }

        return jsonObject;
    }

    /**
     * 기본 배열 키("Result")를 사용하는 getJsonResult 메서드.
     * @param session MyBatis SqlSession 객체
     * @param statement 실행할 MyBatis 쿼리 ID
     * @param param 쿼리에 전달할 매개변수
     * @return 결과를 포함한 JSON 객체
     */
    public ObjectNode getJsonResult(SqlSession session, String statement, Map<String, Object> param) {
        return getJsonResult(session, statement, param, "Result");
    }

    /**
     * 메시지를 추가하여 JSON 결과를 반환하는 메서드.
     * @param session MyBatis SqlSession 객체
     * @param statement 실행할 MyBatis 쿼리 ID
     * @param param 쿼리에 전달할 매개변수
     * @param arrayKey JSON 응답에서 배열로 반환될 키 이름
     * @param message 추가할 메시지
     * @return 결과와 메시지를 포함한 JSON 객체
     */
    public ObjectNode getJsonResultWithMessage(SqlSession session, String statement, Map<String, Object> param, String arrayKey, String message) {
        ObjectNode jsonObject = getJsonResult(session, statement, param, arrayKey); // 기본 결과 생성
        jsonObject.put("message", message); // 메시지 추가
        return jsonObject;
    }

    /**
     * 결과에 따라 동적으로 단일/다중 JSON 응답을 생성하는 메서드.
     * @param session MyBatis SqlSession 객체
     * @param statement 실행할 MyBatis 쿼리 ID
     * @param param 쿼리에 전달할 매개변수
     * @param key JSON 응답에서 데이터가 포함될 키 이름
     * @return 결과를 포함한 JSON 객체
     */
    public ObjectNode getDynamicJsonResult(SqlSession session, String statement, Map<String, Object> param, String key) {
        ObjectNode jsonObject = jsonUtils.createObjectNode(); // 빈 JSON 객체 생성

        try {
            if (session == null) { // SqlSession이 null인 경우 예외 처리
                throw new IllegalArgumentException("SqlSession is null");
            }

            logger.info("Executing MyBatis query: {}", statement);
            List<Map<String, Object>> resultList = session.selectList(statement, param); // MyBatis 쿼리 실행

            if (Optional.ofNullable(resultList).orElse(List.of()).isEmpty()) {
                // 결과가 없는 경우
                logger.info("Query returned no results");
                jsonObject.put("RSLT_CD", "01"); // 실패 코드
                jsonObject.put("message", "No data found for the given query."); // 메시지 추가
                jsonObject.set("data", jsonUtils.createObjectNode()); // 빈 배열로 data 반환
            } else if (resultList.size() == 1) {
                // 단일 결과
                logger.info("Query returned a single result");
                ObjectNode singleResult = jsonUtils.convertMapToJsonObject(resultList.get(0)); // 단일 결과를 JSON 객체로 변환
                jsonObject.put("RSLT_CD", "00"); // 성공 코드
                jsonObject.put("message", "Query executed successfully."); // 메시지 추가
                jsonObject.set("data", singleResult); // 단일 객체로 data 추가
            } else {
                // 다중 결과
                logger.info("Query returned {} results", resultList.size());
                ArrayNode jsonArray = jsonUtils.convertListToJsonArray(resultList); // 결과를 JSON 배열로 변환
                jsonObject.put("RSLT_CD", "00"); // 성공 코드
                jsonObject.put("message", "Query executed successfully."); // 메시지 추가
                jsonObject.set("data", jsonArray); // 배열로 data 추가
            }
        } catch (Exception e) {
            // 쿼리 실행 중 예외 발생
            logger.error("Error executing query: {}", e.getMessage(), e);
            jsonObject.put("RSLT_CD", "99"); // 에러 코드
            jsonObject.put("ERROR_MSG", e.getMessage()); // 에러 메시지 추가
        }

        return jsonObject;
    }
}
