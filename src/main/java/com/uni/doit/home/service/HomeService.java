package com.uni.doit.home.service;

import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HomeService extends BaseService {

    // 홈 List 
    public ResponseEntity<?> homeList(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "HomeList.HomeSelect", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "homeUser");
        }
    }
    
    // Field Item List 
    public ResponseEntity<?> fieldItemList(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "HomeList.FieldItemSelect", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "fieldItemList");
        }
    }
    
    public ResponseEntity<?> catEquipList(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "HomeList.CatEquipSelect", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "catEquipList");
        }
    }
    
    // 타이머 기록 및 보상 처리
    public ResponseEntity<?> timerUser(Map<String, Object> param) {
        try {
            SqlSession session = getSession();

            return ResponseEntity.ok(jsonResultUtils.getJsonResultWithMessage(
            		session, "TimerInsert.InsertTimeReward", param, "Result", "Timer processing completed successfully")
            );
        } catch (Exception e) {
            return handleDatabaseError(e, "timerUser");
        }
    }
}