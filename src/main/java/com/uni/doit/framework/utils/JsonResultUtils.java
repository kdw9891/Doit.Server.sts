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

@Component
public class JsonResultUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonResultUtils.class);
    private final JsonUtils jsonUtils;

    public JsonResultUtils(JsonUtils jsonUtils) {
        this.jsonUtils = jsonUtils;
    }

    public ObjectNode getJsonResult(SqlSession session, String statement, Map<String, Object> param, String arrayKey) {
        ObjectNode jsonObject = jsonUtils.createObjectNode();

        try {
            if (session == null) {
                throw new IllegalArgumentException("SqlSession is null");
            }

            logger.info("Executing MyBatis query: {}", statement);
            List<Map<String, Object>> result = session.selectList(statement, param);

            // Optional로 result 처리
            if (Optional.ofNullable(result).orElse(List.of()).isEmpty()) {
                logger.info("Query returned no results");
                jsonObject.put("RSLT_CD", "01");
            } else {
                logger.info("Query returned {} results", result.size());
                ArrayNode jsonArray = jsonUtils.convertListToJsonArray(result);
                jsonObject.put("RSLT_CD", "00");
                jsonObject.set(arrayKey, jsonArray);
            }
        } catch (Exception e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            jsonObject.put("RSLT_CD", "99");
            jsonObject.put("ERROR_MSG", e.getMessage());
        }

        return jsonObject;
    }

    public ObjectNode getJsonResult(SqlSession session, String statement, Map<String, Object> param) {
        return getJsonResult(session, statement, param, "Result");
    }
    
    public ObjectNode getJsonResultWithMessage(SqlSession session, String statement, Map<String, Object> param, String arrayKey, String message) {
        ObjectNode jsonObject = getJsonResult(session, statement, param, arrayKey);
        jsonObject.put("message", message);
        return jsonObject;
    }
}
