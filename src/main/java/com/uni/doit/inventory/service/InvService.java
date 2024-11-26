package com.uni.doit.inventory.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uni.doit.framework.utils.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class InvService extends BaseService {

    // 카테고리 불러오기
    public ResponseEntity<?> categoryList(Map<String, Object> param) {
        try {
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(getSession(), "InvList.SelectCategory", param, "Result"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("카테고리 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 인벤토리 리스트 불러오기
    public ResponseEntity<?> invList(Map<String, Object> param) {
        try {
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(getSession(), "InvList.SelectInv", param, "Result"));
        } catch (Exception e) {
            return handleDatabaseError(e, "invList");
        }
    }

    // 아이템 사용 처리
    public ResponseEntity<?> handleItemPurchase(Map<String, Object> param) {
        try {
            // 1. 아이템 정보 조회
            ObjectNode itemInfo = jsonResultUtils.getJsonResult(getSession(), "InvList.SelectItemDetails", param);

            System.out.println("Item Info: " + itemInfo.toString());
            System.out.println("Item Category Field: " + itemInfo.get("item_category"));
            
            // Result 배열에서 첫 번째 객체 추출
            if (!itemInfo.has("Result") || !itemInfo.get("Result").isArray() || itemInfo.get("Result").size() == 0) {
                return ResponseEntity.badRequest().body("아이템 정보를 찾을 수 없습니다.");
            }

            // 첫 번째 객체에서 item_category 추출
            ObjectNode itemDetails = (ObjectNode) itemInfo.get("Result").get(0);
            String category = itemDetails.has("item_category") ? itemDetails.get("item_category").asText() : null;
            
            if (category == null) {
                return ResponseEntity.badRequest().body("아이템 카테고리 정보를 확인할 수 없습니다.");
            }

            String userId = param.get("user_id").toString();
            int itemId = Integer.parseInt(param.get("item_id").toString());

            // 2. 카테고리에 따른 로직 처리
            if ("오브젝트".equals(category)) {
                return handleObjectItem(userId, itemId);
            } else if ("의상".equals(category)) {
                return handleWearableItem(userId, itemId);
            } else {
                return ResponseEntity.badRequest().body("처리할 수 없는 아이템 카테고리입니다.");
            }
        } catch (Exception e) {
            return handleDatabaseError(e, "handleItemPurchase");
        }
    }

    private ResponseEntity<?> handleObjectItem(String userId, int itemId) {
        try {
            // 이미 장착된 오브젝트인지 확인
            Map<String, Object> checkParam = Map.of("user_id", userId, "item_id", itemId);
            int count = getSession().selectOne("InvList.FieldObject", checkParam);

            if (count > 0) {
                return ResponseEntity.badRequest().body("이미 장착된 오브젝트입니다.");
            }

            // 오브젝트 삽입
            int result = getSession().insert("InvInsert.InsertObject", checkParam);
            if (result > 0) {
                return ResponseEntity.ok("오브젝트가 성공적으로 장착되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오브젝트 장착에 실패했습니다.");
            }
        } catch (Exception e) {
            return handleDatabaseError(e, "handleObjectItem");
        }
    }

    private ResponseEntity<?> handleWearableItem(String userId, int itemId) {
        try {
            // 이미 장착된 의상인지 확인
            Map<String, Object> checkParam = Map.of("user_id", userId);
            int count = getSession().selectOne("InvList.CatEquipment", checkParam);

            if (count > 0) {
                // 기존 의상 업데이트
                int result = getSession().update("InvUpdate.UpdateWearable", Map.of("user_id", userId, "item_id", itemId));
                if (result > 0) {
                    return ResponseEntity.ok("의상이 성공적으로 변경되었습니다.");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("의상 변경에 실패했습니다.");
                }
            } else {
                // 새 의상 삽입
                int result = getSession().insert("InvInsert.InsertWearable", Map.of("user_id", userId, "item_id", itemId));
                if (result > 0) {
                    return ResponseEntity.ok("의상이 성공적으로 장착되었습니다.");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("의상 장착에 실패했습니다.");
                }
            }
        } catch (Exception e) {
            return handleDatabaseError(e, "handleWearableItem");
        }
    }
}




