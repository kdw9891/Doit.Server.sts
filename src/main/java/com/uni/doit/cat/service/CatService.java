package com.uni.doit.cat.service;

import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CatService extends BaseService {

	public ResponseEntity<?> catUser(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "UserCat.Cat", param, "Result"));
        } catch (Exception e) {
            return handleDatabaseError(e, "catUser");
        }
	}
	
	public ResponseEntity<?> coinUser(Map<String, Object> param) {
		try {
			SqlSession session = getSession();
			return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "UserCoin.Coin", param, "Result"));
		} catch (Exception e) {
			return handleDatabaseError(e, "coinUser");
		}
	}
}