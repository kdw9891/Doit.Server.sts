package com.uni.doit.user.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uni.doit.framework.secure.JwtTokenUtils;
import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.HttpStatus;
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
	            return ResponseEntity.status(401).body("사용자 정보가 없거나 또는 탈퇴된 사용자입니다.");
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
	    SqlSession session = getSession();
	    try {
	    	
	    	int userCount = session.selectOne("RegisterInsert.CheckUserIdExists", param.get("user_id"));
	        if (userCount > 0) {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 사용자 ID입니다.");
	        }
	    	
	        session.insert("RegisterInsert.InsertInitialUserPoints", param);
	        session.insert("RegisterInsert.InsertInitialUserCat", param);
	        
	        ObjectNode result = jsonResultUtils.getJsonResultWithMessage(session, "RegisterInsert.Register", param, "Result", "회원가입이 성공적으로 완료되었습니다.");
	        
	        return ResponseEntity.ok(result);

	    } catch (Exception e) {
	    	return handleDatabaseError(e, "registerUser");
	    }
	}
    
    // 아이디찾기 
    public ResponseEntity<?> idFindUser(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "IdFindCheck.IdFind", param, "Result"));
        } catch (Exception e) {
            return handleDatabaseError(e, "idfindUser");
        }
    }
    
    // 비밀번호 찾기 및 업데이트
    public ResponseEntity<?> passFindUser(Map<String, Object> param) {
        try {
            SqlSession session = getSession();

            ObjectNode result = jsonResultUtils.getJsonResult(session, "PassFindCheck.PassUserFind", param, "Result");

            if (!"00".equals(result.get("RSLT_CD").asText())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다.");
            }

            if (!param.containsKey("password")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("새로운 비밀번호가 입력되지 않았습니다.");
            }

            String newPassword = (String) param.get("password");
            param.put("password", newPassword);

            int updateCount = session.update("PassFindCheck.UpdatePassword", param);

            if (updateCount > 0) {
                return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경에 실패했습니다.");
            }
        } catch (Exception e) {
            return handleDatabaseError(e, "passFindUser");
        }
    }

}
