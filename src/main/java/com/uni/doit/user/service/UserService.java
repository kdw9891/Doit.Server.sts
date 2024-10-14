package com.uni.doit.user.service;

import com.uni.doit.framework.secure.JwtTokenUtils;
import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService extends BaseService {

	public ResponseEntity<?> loginUser(Map<String, Object> param) {
	    try {
	        SqlSession session = getSession();

	        Map<String, Object> userInfo = session.selectOne("LoginList.SelectUserInfo", param);

	        if (userInfo == null || userInfo.isEmpty()) {
	            return ResponseEntity.status(401).body("Invalid credentials");
	        }

	        session.update("LoginList.UpdateLoginDate", param);

	        String token = (String) param.getOrDefault("auth-token", JwtTokenUtils.generateToken(
	                Map.of("user_id", userInfo.get("user_id"), "user_nickname", userInfo.get("user_nickname"))
	        ));

	        Map<String, Object> response = Map.of(
	            "user_id", userInfo.get("user_id"),
	            "user_nickname", userInfo.get("user_nickname"),
	            "email", userInfo.get("email"),
	            "use_yn", userInfo.get("use_yn"),
	            "auth-token", token
	        );

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        return handleDatabaseError(e, "loginUser");
	    }
	}

    // 회원가입
    public ResponseEntity<?> registerUser(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "RegisterInsert.Register", param, "Result"));
        } catch (Exception e) {
            return handleDatabaseError(e, "registerUser");
        }
    }
    
    // 아이디찾기 
    public ResponseEntity<?> idfindUser(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "IdfindCheck.Idfind", param, "Result"));
        } catch (Exception e) {
            return handleDatabaseError(e, "idfindUser");
        }
    }
}
