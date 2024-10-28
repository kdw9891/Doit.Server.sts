package com.uni.doit.inventory.controller;

import com.uni.doit.inventory.dto.*;
import com.uni.doit.inventory.service.InvService;
import com.uni.doit.framework.utils.BaseController;

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

    @GetMapping("/cat")
    public ResponseEntity<?> catUser(@RequestBody InvRequest catRequest) throws IOException {
        ResponseEntity<Map<String, Object>> validationResult = validateDto(catRequest, "user_id", "inventory_id", "item_id", "quentity");

        if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }

        Map<String, Object> params = validationResult.getBody();

        return invService.invList(params);
    }
    
}