package com.uni.doit.rank.controller;

import com.uni.doit.framework.utils.BaseController;
import com.uni.doit.framework.utils.ParamUtils;
import com.uni.doit.rank.service.RankService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Tag(name = "rank")
@RestController
@RequestMapping("/rank")
public class RankController extends BaseController {
	
	@Autowired
    private RankService rankService;

    public RankController(RankService rankService) {
        this.rankService = rankService;
    }
    
    // select 하는 해당 날짜의 주차 랭킹 list
    @GetMapping("/list")
    public ResponseEntity<?> HomeList(@RequestParam String user_id) throws IOException {
    	Map<String, Object> params = ParamUtils.createParams("user_id", user_id);       
        
        return rankService.rankList(params);
    }
    
    // 주차 선택하여 리스트 보내주는 서비스
    @GetMapping("/history")
    public ResponseEntity<?> WeekList(@RequestParam String user_id, String search_date) throws IOException {
    	Map<String, Object> params = ParamUtils.createParams("user_id", user_id, "search_date", search_date);       
        
        return rankService.rankHistory(params);
    }
    
}
