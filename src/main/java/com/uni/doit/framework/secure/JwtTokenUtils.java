package com.uni.doit.framework.secure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtTokenUtils {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // HMAC-SHA256 비밀 키 생성
    private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7; // 7일(7 * 24 * 60 * 60 * 1000ms)

    /**
     * 외부에서 전달된 클레임(claims)와 함께 JWT 토큰을 생성합니다.
     * @param claims JWT 페이로드에 담을 데이터 (키-값 쌍)
     * @return 생성된 JWT 토큰 문자열
     */
    public static String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME); // 7일 후 만료

        return Jwts.builder()
                .setClaims(claims) // 페이로드 데이터 설정
                .setIssuedAt(now) // 발행 시간 설정
                .setExpiration(expirationDate) // 만료 시간 설정
                .signWith(SECRET_KEY) // 서명 정보 설정 (HS256 알고리즘 사용)
                .compact(); // 토큰 생성
    }

    /**
     * JWT 토큰을 파싱하여 클레임 데이터를 추출합니다.
     * @param token JWT 토큰 문자열
     * @return JWT 클레임 데이터
     */
    public static Map<String, Object> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * JWT 토큰의 유효성을 검증합니다.
     * @param token JWT 토큰 문자열
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // 유효하지 않은 경우
        }
    }
}
