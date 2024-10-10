package com.uni.doit.user.controller;

import com.uni.doit.framework.utils.ParamUtils;
import com.uni.doit.user.dto.RegisterRequest;
import com.uni.doit.user.service.UserService;
import com.uni.doit.framework.utils.BaseController;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Tag(name = "user")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String user_id, @RequestParam String password, HttpServletRequest request) throws IOException {
        Map<String, Object> params = ParamUtils.createParams("user_id", user_id, "password", password);

        String existingToken = extractAuthToken(request);
        if (existingToken != null) {
            params.put("auth-token", existingToken);
        }

        return userService.loginUser(params);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) throws IOException {
        ResponseEntity<Map<String, Object>> validationResult = validateDto(registerRequest, "user_id", "password", "user_nickname", "email");

        if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }

        Map<String, Object> params = validationResult.getBody();

        return userService.registerUser(params);
    }
}
