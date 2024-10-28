package com.uni.doit.inventory.service;

import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InvService extends BaseService {
	
	public ResponseEntity<?> invList(Map<String, Object> param) {
		try {
			SqlSession session = getSession();
			return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "InvList.SelectInv", param, "Result"));
		} catch (Exception e) {
			return handleDatabaseError(e, "invList");
		}
	}
	
	public ResponseEntity<?> useItem(Map<String, Object> param) {
	    try {
	    	SqlSession session = getSession();

	        Map<String, Object> itemInfo = session.selectOne("InvList.SelectInv", param);
	        
	        if (itemInfo == null || (int) itemInfo.get("is_used") != 1) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이템을 사용할 수 없습니다.");
	        }

	        int quantity = (int) itemInfo.get("quantity");
	        // 수량 확인 
	        if (quantity > 0) {

	            session.update("IvnDelete.DeleteItem", param);

	            session.insert("IvnDelete.InsertUseItem", param);

	            return ResponseEntity.ok("아이템 사용이 완료되었습니다.");
	            
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이템 수량이 부족합니다.");
	        }
	    } catch (Exception e) {
	        return handleDatabaseError(e, "useItem");
	    } 
	    
	    }
}


