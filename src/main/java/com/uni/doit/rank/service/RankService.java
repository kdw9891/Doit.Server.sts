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
    
    // 주차 및 날짜 선택하여 주차별 랭킹 history list 서비스 - 쿼리 추가 필요
    public ResponseEntity<?> RankHistory(Map<String, Object> param) {
        SqlSession session = null;
        try {
            session = getSession();

            //파라미터로 가져오기 
            Integer weeknumber = (Integer) param.get("week_number");
            param.put("week_number", weeknumber);

            //해당 주차의 랭킹 기록 조회
            Map<String, Object> rankHistory = session.selectOne("RankList.SelectRank", param);

            if (rankHistory == null || rankHistory.isEmpty()) {
                // 기록이 없는 경우
                return ResponseEntity.ok("해당 주차의 랭킹 기록이 없습니다.");
            } else {
                // 기록이 있는 경우
            	return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "RankList.SelectRank", param, "Result"));
            }
        } catch (Exception e) {
            return handleDatabaseError(e, "RankHistory");
        }
    }


}
