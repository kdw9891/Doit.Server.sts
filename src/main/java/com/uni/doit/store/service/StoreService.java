package com.uni.doit.store.service;

import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StoreService extends BaseService {

	// 카테고리 List
	public ResponseEntity<?> categoryList(Map<String, Object> param) {
        try {
        	SqlSession session = getSession();
            
        	return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "StoreList.SelectCategory", param, "Result"));
        	
        } catch (Exception e) {
            return ResponseEntity.status(500).body("카테고리 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }
	
	// 아이템 List
    public ResponseEntity<?> itemList(Map<String, Object> param) {
        try {
        	SqlSession session = getSession();
            
        	return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "StoreList.SelectItem", param, "Result"));
        	
        } catch (Exception e) {
            return ResponseEntity.status(500).body("아이템 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }
	
    // 아이템 구매
    public ResponseEntity<?> purchaseItem(Map<String, Object> params) {
        try {
            SqlSession session = getSession();

            // 아이템 정보 확인
            Map<String, Object> itemInfo = session.selectOne("StoreList.SelectItemPrice", params);
            if (itemInfo == null || (int) itemInfo.get("is_available") != 1) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("해당 아이템은 구매할 수 없습니다.");
            }

            // 아이템 가격 및 총 비용 계산
            int itemPrice = (int) itemInfo.get("item_price");
            int quantity = (int) params.getOrDefault("quantity", 1); // 기본값 1
            if (quantity <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 수량입니다.");
            }
            int totalCost = itemPrice * quantity;

            // 사용자 포인트 확인
            int userPoints = session.selectOne("StoreInsert.SelectUserTotalPoints", params);
            if (userPoints < totalCost) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("포인트가 부족합니다.");
            }

            // 포인트 차감
            params.put("points_spent", totalCost);
            session.update("StoreInsert.UpdateUserTotalPoints", params);

            // 인벤토리에 아이템 추가
            session.insert("StoreInsert.InsertUserInventory", params);

            // 포인트 사용 내역 기록
            session.insert("StoreInsert.InsertPointUsage", params);

            return ResponseEntity.ok("구매가 완료되었습니다.");
        } catch (Exception e) {
            logger.error("구매 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("구매 중 오류 발생: " + e.getMessage());
        }
    }

}
