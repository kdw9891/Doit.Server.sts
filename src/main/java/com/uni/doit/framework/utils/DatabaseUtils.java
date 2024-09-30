package com.uni.doit.framework.utils;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DatabaseUtils {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);
    private final SqlSessionTemplate sqlSessionTemplate;

    public DatabaseUtils(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    // SqlSessionTemplate을 이용하여 명시적인 세션 열기/닫기 없이 사용
    public SqlSession getSession() {
        try {
            logger.info("Retrieving SqlSession from SqlSessionTemplate");
            return sqlSessionTemplate;
        } catch (Exception e) {
            logger.error("Error retrieving SqlSession: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve SqlSession", e);
        }
    }
}
