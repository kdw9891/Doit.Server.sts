package com.uni.doit.cat.controller;

import com.uni.doit.cat.dto.*;
import com.uni.doit.cat.service.CatService;
import com.uni.doit.framework.utils.BaseController;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Tag(name = "home")
@RestController
@RequestMapping("/home")
public class CatController extends BaseController {
	
    @Autowired
    private CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @GetMapping("/cat")
    public ResponseEntity<?> catUser(@RequestBody CatRequest catRequest) throws IOException {
        ResponseEntity<Map<String, Object>> validationResult = validateDto(catRequest, "user_id", "cat_level", "xp_total");

        if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }

        Map<String, Object> params = validationResult.getBody();

        return catService.catUser(params);
    }
    
    @GetMapping("/coin")
    public ResponseEntity<?> coinUser(@RequestBody CoinRequest coinRequest) throws IOException {
        ResponseEntity<Map<String, Object>> validationResult = validateDto(coinRequest, "user_id", "total_points");

        if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }

        Map<String, Object> params = validationResult.getBody();
        
        return catService.coinUser(params);
    }
}