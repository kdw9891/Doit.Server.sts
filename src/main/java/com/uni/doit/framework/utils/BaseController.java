package com.uni.doit.framework.utils;

import com.uni.doit.framework.secure.JwtTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RequestUtils requestUtils;

    // 공통적인 DTO 유효성 검사 메서드
    protected ResponseEntity<Map<String, Object>> validateDto(Object dto, String... requiredFields) {
        return requestUtils.validateDtoParams(dto, requiredFields);
    }

    // 공통적인 Authorization 헤더 추출 메서드
    protected String extractAuthToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // API 호출 시 토큰 검증 메서드 (Spring Security 미사용)
    protected boolean validateToken(String token) {
        try {
            // JWT 토큰 유효성 검증 로직 (예: 서명 검증, 만료 시간 확인 등)
            return JwtTokenUtils.validateToken(token);
        } catch (Exception e) {
            logger.error("Invalid token: {}", e.getMessage(), e);
            return false;
        }
    }

    // API 호출 시 토큰의 유효성 검증을 공통적으로 수행하는 메서드
    protected boolean isAuthenticated(HttpServletRequest request) {
        String token = extractAuthToken(request);
        return token != null && validateToken(token);
    }

    // 공통적인 에러 처리 메서드
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        logger.error("Error occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
    }
}
