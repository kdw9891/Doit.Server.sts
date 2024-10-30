package com.uni.doit.rank.service;

import java.util.Map;

import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.ResponseEntity;

public class RankService extends BaseService {
	// 홈 List 
    public ResponseEntity<?> rankList(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "RankList.SelectRank", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "rank");
        }
    }
    
//    // 주차 및 날짜 선택하여 주차별 랭킹 history list 서비스 - 쿼리 추가 필요
//    public ResponseEntity<?> timerUser(Map<String, Object> param) {
//        try {
//            SqlSession session = getSession();
//
//            return ResponseEntity.ok(jsonResultUtils.getJsonResultWithMessage(
//            		session, "TimerInsert.InsertTimeReward", param, "Result", "Timer processing completed successfully")
//            );
//        } catch (Exception e) {
//            return handleDatabaseError(e, "timerUser");
//        }
//    }
}
