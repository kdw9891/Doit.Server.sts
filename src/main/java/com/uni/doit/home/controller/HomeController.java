package com.uni.doit.home.controller;

import com.uni.doit.framework.utils.BaseController;
import com.uni.doit.framework.utils.ParamUtils;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    public ResponseEntity<?> HomeList(@RequestParam String user_id) throws IOException {
    	Map<String, Object> params = ParamUtils.createParams("user_id", user_id);       
        
        return homeService.homeList(params);
    }
    
    @GetMapping("/timer")
    public ResponseEntity<?> TimerUser(@RequestParam String user_id, Integer study_time) throws IOException {
    	Map<String, Object> params = ParamUtils.createParams("user_id", user_id, "study_time", study_time);
        
        return homeService.timerUser(params);
    }
}
