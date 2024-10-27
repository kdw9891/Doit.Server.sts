package com.uni.doit.home.controller;

import com.uni.doit.framework.utils.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uni.doit.home.dto.*;
import com.uni.doit.home.service.HomeService;

import java.io.IOException;
import java.util.Map;

@Tag(name = "home")
@RestController
@RequestMapping("/home")
public class HomeController extends BaseController {

    @Autowired
    private HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home")
    public ResponseEntity<?> HomeUser(@RequestBody HomeRequest homeRequest) throws IOException {
        ResponseEntity<Map<String, Object>> validationResult = validateDto(homeRequest, "user_id", "cat_level", "xp_total", "total_points");

        if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }
        
        Map<String, Object> params = validationResult.getBody();
        
        return homeService.homeUser(params);
    }
    
    @GetMapping("/timer")
    public ResponseEntity<?> TimerUser(@RequestBody TimerRequest timerRequest) throws IOException {
        ResponseEntity<Map<String, Object>> validationResult = validateDto(timerRequest, "user_id", "week_number", "total_study_time");

        if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }
        
        Map<String, Object> params = validationResult.getBody();
        
        return homeService.timerUser(params);
    }
}
