package com.uni.doit.inventory.controller;

import com.uni.doit.inventory.service.InvService;
import com.uni.doit.framework.utils.BaseController;
import com.uni.doit.framework.utils.ParamUtils;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Tag(name = "inventory")
@RestController
@RequestMapping("/inventory")
public class InvController extends BaseController {
	
    @Autowired
    private InvService invService;

    public InvController(InvService invService) {
        this.invService = invService;
    }
    
    // 카테고리 리스트
    @GetMapping("/category")
    public ResponseEntity<?> categoryList() throws IOException {
    	Map<String, Object> params = ParamUtils.createParams();
        
        return invService.categoryList(params);
    }
    
    // 인벤토리 리스트
    @GetMapping("/list")
    public ResponseEntity<?> invUser(@RequestParam String user_id) throws IOException {
    	Map<String, Object> params = ParamUtils.createParams("user_id", user_id);

        return invService.invList(params);
    }
 
    // 아이템 사용
    @PutMapping("/itemuse")
    public ResponseEntity<?> useItem(@RequestParam String user_id, String inventory_id, Integer item_id) {
    	Map<String, Object> params = ParamUtils.createParams("user_id", user_id, "inventory_id", inventory_id, "item_id", item_id);
    	
        return invService.handleItemPurchase(params);
    }
    
}