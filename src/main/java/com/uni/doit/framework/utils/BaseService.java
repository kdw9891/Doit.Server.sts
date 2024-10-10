package com.uni.doit.framework.utils;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public abstract class BaseService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected JsonResultUtils jsonResultUtils;

    @Autowired
    protected DatabaseUtils databaseUtils;

    protected SqlSession getSession() {
        return databaseUtils.getSession();
    }

    protected ResponseEntity<?> handleDatabaseError(Exception e, String action) {
        logger.error("Database error during {}: {}", action, e.getMessage(), e);
        return ResponseEntity.status(500).body("Database error: " + e.getMessage());
    }
}
