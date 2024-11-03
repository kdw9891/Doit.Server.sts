package com.uni.doit.store.controller;

import com.uni.doit.framework.utils.BaseController;
import com.uni.doit.framework.utils.ParamUtils;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uni.doit.store.dto.*;
import com.uni.doit.store.service.StoreService;

import java.io.IOException;
import java.util.Map;

@Tag(name = "store")
@RestController
@RequestMapping("/store")
public class StoreController extends BaseController {

    @Autowired
    private StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }
    
    // 카테고리
    @GetMapping("/category")
    public ResponseEntity<?> CategoryList() throws IOException {
    	Map<String, Object> params = ParamUtils.createParams();
        
        return storeService.itemList(params);
    }
    
    // 아이템
    @GetMapping("/items")
    public ResponseEntity<?> ItemList(@RequestParam String item_category) throws IOException {
    	Map<String, Object> params = ParamUtils.createParams("item_category", item_category);
        
        return storeService.categoryList(params);
    }

    /**
     * @param quantity 아이템 수량
     * 아이템 구매 
     */
    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseItem(@RequestBody PurchaseRequest purchaseRequest) throws IOException {
        ResponseEntity<Map<String, Object>> validationResult = validateDto(purchaseRequest, "user_id", "item_id", "quantity");

        if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }
        
        Map<String, Object> params = validationResult.getBody();
        
        return storeService.purchaseItem(params);
    }
    
}
