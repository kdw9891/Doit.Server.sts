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
            int result = session.selectOne("UserCat.Cat", param);
        if (result > 0) {
        	return ResponseEntity.ok("고양이를 성공적으로 불러왔습니다.");
        } else {
        	session.insert("UserCat.DefaultCat", param);
            return ResponseEntity.ok("기본 고양이가 성공적으로 생성되었습니다.");
        }
        } 
        
        catch (Exception e) {
            return handleDatabaseError(e, "catUser");
        }
	}
	
	//코인 
	public ResponseEntity<?> coinUser(Map<String, Object> param) {
		try {
			SqlSession session = getSession();
			return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "UserCoin.Coin", param, "Result"));
		} catch (Exception e) {
			return handleDatabaseError(e, "coinUser");
		}
	}


}