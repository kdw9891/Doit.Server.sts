package com.uni.doit.store.service;

import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StoreService extends BaseService {

    public ResponseEntity<?> itemList(Map<String, Object> param) {
        try {
        	SqlSession session = getSession();
            
        	return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "StoreList.SelectItem", param, "Result"));
        	
        } catch (Exception e) {
            return ResponseEntity.status(500).body("아이템 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }
	
	public ResponseEntity<?> purchaseItem(Map<String, Object> params) {
	    try {
	    	SqlSession session = getSession();
	        // 1. 아이템 정보 확인
	        Map<String, Object> itemInfo = session.selectOne("StoreList.SelectItemPrice", params);
	        if (itemInfo == null || (int) itemInfo.get("is_available") != 1) {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("해당 아이템은 구매할 수 없습니다.");
	        }
	        
	        int itemPrice = (int) itemInfo.get("item_price");
	        int quantity = (int) params.get("quantity");
	        int totalCost = itemPrice * quantity;

	        int userPoints = session.selectOne("StoreInsert.SelectUserTotalPoints", params);
	        
	        if (userPoints < totalCost) {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("포인트가 부족합니다.");
	        }

	        params.put("points_spent", totalCost);
	        session.update("StoreInsert.UpdateUserTotalPoints", params);

	        session.insert("InvList.InsertUserInventory", params);

	        session.insert("StoreInsert.InsertPointUsage", params);

	        return ResponseEntity.ok("구매가 완료되었습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("구매 중 오류 발생: " + e.getMessage());
	    }
	}

}
