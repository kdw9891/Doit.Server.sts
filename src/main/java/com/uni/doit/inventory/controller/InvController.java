package com.uni.doit.inventory.controller;

import com.uni.doit.inventory.dto.*;
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
    @GetMapping("/inv")
    public ResponseEntity<?> invUser(@RequestBody InvRequest invRequest) throws IOException {
        ResponseEntity<Map<String, Object>> validationResult = validateDto(invRequest, "user_id", "inventory_id", "item_id");

        if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }

        Map<String, Object> params = validationResult.getBody();

        return invService.invList(params);
    }
    
    // 소모품 사용
    @PostMapping("/uses")
    public ResponseEntity<?> useItem(@RequestBody UseRequest useRequest) throws IOException {
        ResponseEntity<Map<String, Object>> validationResult = validateDto(useRequest, "user_id", "item_id", "quantity");

        if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }

        Map<String, Object> params = validationResult.getBody();

        return invService.useItem(params);
    }
    
    // 아이템 착용
    @PostMapping("/equips")
    public ResponseEntity<?> equipItem(@RequestBody UseRequest useRequest) throws IOException {
        ResponseEntity<Map<String, Object>> validationResult = validateDto(useRequest, "user_id", "item_id", "quantity");

        if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }

        Map<String, Object> params = validationResult.getBody();

        return invService.equipItem(params);
    }
}