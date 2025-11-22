package Xthon.gAIde.util.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import Xthon.gAIde.exception.CustomException;
import Xthon.gAIde.exception.ErrorCode;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secretKey;
    private final Long expireMs = 1000L * 60 * 60; // 1시간

    public JwtUtil(@Value("${jwt.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    private Key getKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // loginId로 토큰 생성
    public String createJwt(String loginId) {
        Claims claims = Jwts.claims();
        // ★ 중요: 여기서 넣은 키 이름("loginId")을 기억하세요
        claims.put("loginId", loginId);

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expireMs))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_UNSUPPORTED);
        } catch (MalformedJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_MALFORMED);
        } catch (SignatureException e) {
            throw new CustomException(ErrorCode.TOKEN_MALFORMED);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.TOKEN_UNKNOWN);
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.TOKEN_UNKNOWN);
        }
    }

    // 토큰에서 loginId 추출
    public String getloginId(String token) {
        // ★ 수정됨: "LoginId" -> "loginId" (대소문자 일치)
        return parseClaims(token).get("loginId", String.class);
    }
}