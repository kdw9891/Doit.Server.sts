package com.uni.doit.home.service;

import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HomeService extends BaseService {

    // home 
    public ResponseEntity<?> homeUser(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "Home.UserHome", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "homeUser");
        }
    }
    
    // 타이머 
    public ResponseEntity<?> timerUser(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "Timer.StudyTime", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "timerUser");
        }
    }
}