package com.uni.doit.inventory.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
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
	
	// 아이템 사용
	public ResponseEntity<?> useItem(Map<String, Object> param) {
        try {
        	SqlSession session = getSession();
            // 아이템 정보 조회
            ObjectNode itemInfo = jsonResultUtils.getJsonResult(session, "InvList.SelectInv", param);

            // 아이템이 존재하지 않을 경우
            if (!"00".equals(itemInfo.get("RSLT_CD").asText())) {
                return ResponseEntity.badRequest().body("아이템을 사용할 수 없습니다.");
            }

            // 카테고리에 따라 처리
            String category = itemInfo.has("item_category") ? itemInfo.get("item_category").asText() : null;
            System.out.println("Item Category: " + category);
            if ("소비".equals(category)) {
                return consumeItem(session, param);
            } else if ("의상".equals(category)) {
                return equipItem(session, param, "EquipWearableItem");
            } else if ("오브젝트".equals(category)) {
                return equipItem(session, param, "EquipObjectItem");
            } else {
                return ResponseEntity.badRequest().body("알 수 없는 아이템 카테고리입니다.");
            }
        } catch (Exception e) {
            return handleDatabaseError(e, "useItem");
        }
    }

    // 소비 아이템 사용 시 로직
    private ResponseEntity<?> consumeItem(SqlSession session, Map<String, Object> param) {
        try {
            // 아이템 수량 감소 및 사용 기록 추가
            session.update("InvUpdate.ConsumeItem", param);
            session.insert("InvInsert.InsertUseLog", param);

            // 사용 후 수량이 0이면 삭제
            session.delete("InvDelete.DeleteZeroQuantityItem", param);

            return ResponseEntity.ok("소비 아이템 사용이 완료되었습니다.");
        } catch (Exception e) {
            return handleDatabaseError(e, "consumeItem");
        }
    }
    
    // 오브젝트 및 착장템 착용 로직
    private ResponseEntity<?> equipItem(SqlSession session, Map<String, Object> param, String equipStatement) {
        try {
            // 현재 착용 중인지 확인
            ObjectNode equipInfo = jsonResultUtils.getJsonResult(session, "InvList.SelectInv", param);
            if ("1".equals(String.valueOf(equipInfo.get("is_equipped")))) {
                return ResponseEntity.ok("이미 착용 중인 아이템입니다.");
            }

            // 새 아이템 착용
            session.update("InvUpdate." + equipStatement, param);
            return ResponseEntity.ok("아이템이 착용되었습니다.");
        } catch (Exception e) {
            return handleDatabaseError(e, "equipItem");
        }
    }
}



