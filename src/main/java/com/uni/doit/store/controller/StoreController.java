package com.uni.doit.store.controller;

import com.uni.doit.framework.utils.BaseController;
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

    @PostMapping("/items")
    public ResponseEntity<?> ItemList(@RequestBody ItemRequest itemRequest) throws IOException {
        ResponseEntity<Map<String, Object>> validationResult = validateDto(itemRequest, "item_category");

        if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }
        
        Map<String, Object> params = validationResult.getBody();
        
        return storeService.itemList(params);
    }

    /**
     * @param quantity 아이템 수량 
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
