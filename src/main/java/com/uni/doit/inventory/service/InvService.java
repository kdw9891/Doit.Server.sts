package com.uni.doit.inventory.service;

import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InvService extends BaseService {
	
	//카테고리 불러오기 
	public ResponseEntity<?> categoryList(Map<String, Object> param) {
        try {
        	SqlSession session = getSession();
            
        	return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "InvList.SelectCategory", param, "Result"));
        	
        } catch (Exception e) {
            return ResponseEntity.status(500).body("카테고리 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }
	
	// 인벤토리 리스트 불러오기 
	public ResponseEntity<?> invList(Map<String, Object> param) {
		try {
			SqlSession session = getSession();
			return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "InvList.SelectInv", param, "Result"));
		} catch (Exception e) {
			return handleDatabaseError(e, "invList");
		}
	}
	
	//소모품 사용 
	public ResponseEntity<?> useItem(Map<String, Object> param) {
	    try {
	        SqlSession session = getSession();

	        // 아이템 정보 조회
	        Map<String, Object> useInfo = session.selectOne("InvList.SelectInv", param);

	        // 아이템이 존재하지 않을 경우 
	        if (useInfo == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이템을 사용할 수 없습니다.");
	        }

	        int quantity = (int) useInfo.get("quantity");
	        	        
	        if (quantity > 0) {
	            // 수량 감소 및 사용 기록 추가
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
	
	// 아이템 착용 
	public ResponseEntity<?> equipItem(Map<String, Object> param) {
	    try {
	        SqlSession session = getSession();

	        // 아이템 정보 조회
	        Map<String, Object> itemInfo = session.selectOne("Inventory.SelectItem", param);

	        // 아이템이 존재하지 않는 경우
	        if (itemInfo == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이템이 존재하지 않습니다.");
	        }

	       // 아이템 수량 확인
	        int quantity = (int) itemInfo.get("quantity");
	        if (quantity < 0) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이템 수량이 부족합니다.");
	        }

	        // 현재 착용 중인 아이템 조회
	        Map<String, Object> equipInfo = session.selectOne("Inventory.EquipItem", param);

	        // 착용 상태 확인
	        if (equipInfo != null && "1".equals(equipInfo.get("is_equipped").toString())) {
	            return ResponseEntity.ok("이미 착용 중인 아이템입니다.");
	        }

	        // 새로운 아이템 착용 처리
	        param.put("is_equipped", "1");
	        session.update("Inventory.EquipItem", param); // 새 아이템 착용

	        return ResponseEntity.ok("아이템이 착용되었습니다.");
	    } catch (Exception e) {
	        return handleDatabaseError(e, "equipItem");
	    }
	}
}



