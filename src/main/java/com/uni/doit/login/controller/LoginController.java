package com.uni.doit.login.controller;

import com.uni.doit.framework.utils.ParamUtils;
import com.uni.doit.framework.utils.RequestUtils;
import com.uni.doit.user.dto.LoginRequest;
import com.uni.doit.login.service.LoginService;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * UserController: 사용자 관련 API를 제공하는 컨트롤러 클래스.
 * - 사용자의 로그인 및 경로 정보를 처리하는 REST API 엔드포인트를 정의합니다.
 * - DTO 기반의 요청 처리와, @RequestParam을 통해 파라미터를 직접 받아 처리하는 두 가지 방식의 로그인 API를 제공합니다.
 * - 또한, 현재 서버의 Context Path 정보를 반환하는 엔드포인트도 제공합니다.
 */
@Tag(name = "login")  // Swagger와 같은 API 문서화 도구에서 이 컨트롤러의 태그를 설정하는 어노테이션
@RestController  // 이 클래스가 REST API 요청을 처리하는 컨트롤러임을 나타냄
@RequestMapping("/login")  // 이 컨트롤러의 기본 경로를 설정합니다. "/login" 경로로 들어오는 요청을 처리합니다.
public class LoginController {

	@Autowired
    private LoginService loginService;  // 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
    
	@Autowired
    private RequestUtils requestUtils;  // 파라미터 유효성 검사 등 요청 관련 유틸리티 클래스

    // UserController의 생성자: userService와 requestUtils를 주입 받습니다.
    public LoginController(LoginService loginService, RequestUtils requestUtils) {
        this.loginService = loginService;
        this.requestUtils = requestUtils;
    }
    
    /**
     * /dto/login: 사용자 로그인 API (DTO 기반).
     * 
     * @param loginRequest: LoginRequest DTO를 요청 본문에서 받아와 사용합니다.
     *                      corp_id, fac_id, user_id를 포함해야 합니다.
     * @return 유효성 검사 후, 로그인 처리 결과를 반환합니다.
     * @throws IOException: 입출력 예외가 발생할 수 있습니다.
     * 
     * - 이 메서드는 @RequestBody를 사용하여 클라이언트가 JSON 본문으로 데이터를 전송할 때 처리합니다.
     * - RequestUtils의 validateDtoParams 메서드를 사용하여 필수 필드(corp_id, fac_id, user_id)의 유효성을 검사합니다.
     * - 유효성 검사에 통과하면, service 계층으로 요청을 전달하여 로그인 로직을 처리합니다.
     */
    @PostMapping("/dto/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) throws IOException {

        // DTO 파라미터 유효성 검사: corp_id, fac_id, user_id 필드를 검사.
        ResponseEntity<Map<String, Object>> validationResult = requestUtils.validateDtoParams(loginRequest, "user_id", "password");
        
        // 유효성 검사 실패 시, 4xx 에러 응답 반환.
        if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;  // 파라미터 오류 처리
        }

        // 유효성 검사 성공 시, DTO에서 추출한 파라미터를 Map으로 받아 서비스 레이어에 전달.
        Map<String, Object> params = validationResult.getBody();
        
        return loginService.loginUser(params);  // 서비스 로직 호출 및 결과 반환
    }
    
    /**
     * /param/login: 사용자 로그인 API (RequestParam 기반).
     * 
     * @param corp_id: 클라이언트가 쿼리 스트링 또는 폼 데이터로 보낸 corp_id 파라미터.
     * @param fac_id: 클라이언트가 보낸 fac_id 파라미터.
     * @param user_id: 클라이언트가 보낸 user_id 파라미터.
     * @return 로그인 처리 결과를 반환합니다.
     * @throws IOException: 입출력 예외가 발생할 수 있습니다.
     * 
     * - 이 메서드는 @RequestParam을 사용하여 클라이언트가 쿼리 스트링이나 폼 데이터를 통해 파라미터를 전달할 때 처리합니다.
     * - 클라이언트는 파라미터를 URL 쿼리 스트링 또는 x-www-form-urlencoded 방식으로 보내야 합니다.
     * - ParamUtils.createParams를 사용하여 파라미터들을 Map<String, Object>로 변환하여 로그인 로직에 전달합니다.
     */
    @PostMapping("/param/login")
    public ResponseEntity<?> testloginUser(@RequestParam String user_id, @RequestParam String password) throws IOException {

        // RequestParam으로 전달된 파라미터들을 Map으로 변환
        Map<String, Object> params = ParamUtils.createParams("user_id", user_id, "password", password);
        
        return loginService.loginUser(params);  // 서비스 로직 호출 및 결과 반환
    }
    
    /**
     * /path: 서버의 Context Path 정보를 반환하는 API.
     * 
     * @param request: HttpServletRequest 객체를 통해 현재 요청의 정보를 가져옴.
     * @return Context Path 정보를 담은 Map을 반환.
     * 
     * - 이 메서드는 클라이언트에게 현재 서버의 Context Path를 반환합니다.
     * - Context Path는 애플리케이션이 배포된 경로를 의미하며, 예를 들어 "/mooroc"과 같은 값을 반환할 수 있습니다.
     */
    @GetMapping("/path")
    public ResponseEntity<Map<String, String>> getContextPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();  // 현재 서버의 Context Path를 가져옴
        Map<String, String> response = new HashMap<>();
        response.put("contextPath", contextPath);  // Map에 추가
        return ResponseEntity.ok(response);  // Context Path 정보를 클라이언트에게 반환
    }

}
