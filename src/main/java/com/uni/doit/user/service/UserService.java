package com.uni.doit.user.service;

import com.uni.doit.framework.utils.DatabaseUtils;
import com.uni.doit.framework.utils.JsonResultUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final JsonResultUtils jsonResultUtils;
    private final DatabaseUtils databaseUtils;

    public UserService(JsonResultUtils jsonResultUtils, DatabaseUtils databaseUtils) {
        this.jsonResultUtils = jsonResultUtils;
        this.databaseUtils = databaseUtils;
    }

    public ResponseEntity<?> loginUser(Map<String, Object> param) {
        try {
            SqlSession session = databaseUtils.getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "UserList.SelectUserInfo", param, "userinfo"));
        } catch (Exception e) {
            logger.error("Database error during loginUser: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }
}
