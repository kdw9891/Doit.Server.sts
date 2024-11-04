package com.uni.doit.rank.service;

import java.util.Map;

import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RankService extends BaseService {
	
	// 랭크 List
    public ResponseEntity<?> rankList(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "RankList.SelectRank", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "rank");
        }
    }
    
    // 주차 및 날짜 선택하여 주차별 랭킹 history list 서비스
    public ResponseEntity<?> rankHistory(Map<String, Object> param) {
        try {
        	SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "RankList.SelectWeekRank", param, "Result"));
        } 
        catch (Exception e) {
            return handleDatabaseError(e, "rankHistory");
        }
    }


}
