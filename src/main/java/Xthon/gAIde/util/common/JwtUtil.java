package Xthon.gAIde.util.common;

import io.jsonwebtoken.*; // JwtException 등 임포트
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; // SignatureException 임포트
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import Xthon.gAIde.exception.CustomException; // CustomException 임포트
import Xthon.gAIde.exception.ErrorCode; // ErrorCode 임포트

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

//JWT의 생성, 검증, 정보 추출을 담당하는 유틸리티 클래스
@Component
public class JwtUtil {

    private final String secretKey;                     // JWT는 위변조가 불가능하도록 final 사용
    private final Long expireMs = 1000L * 60 * 60;      // 토큰 유효 시간 == 1시간(밀리초 단위)

    //yml의 'jwt.secret-key' 값이 @Value에 의해 주입됨
    public JwtUtil(@Value("${jwt.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    //yml에서 읽어 온 문자열을 키로 변환
    private Key getKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);   // 키를 UTF8 바이트 배열로 변경
        return Keys.hmacShaKeyFor(keyBytes);                            // HMAC-SHA에 맞는 Key 생성
    }

    // 학번을 받아서 JWT 생성
    public String createJwt(String loginId) {
        Claims claims = Jwts.claims();              // 토큰안에 담을 정보
        claims.put("loginId", loginId);                   // 유저 아이디
        long now = System.currentTimeMillis();      // 발급 시간

        // 토큰 조립
        return Jwts.builder()
                .setClaims(claims)                              // Claims 탑재
                .setIssuedAt(new Date(now))                     // 토큰 발행 시간
                .setExpiration(new Date(now + expireMs))        // 토큰 만료 시간
                .signWith(getKey(), SignatureAlgorithm.HS256)   // 키를 가져와 HS256으로 서명
                .compact();                                     // JWT 생성
    }

    // 프론트엔드에서 받은 JWT 문자열을 Claims 정보로 파싱
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);             // 토큰 만료
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_UNSUPPORTED);         // 지원되지 않는 토큰
        } catch (MalformedJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_MALFORMED);           // 잘못된 형식
        } catch (SignatureException e) {
            throw new CustomException(ErrorCode.TOKEN_MALFORMED);           // 위조 예외
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.TOKEN_UNKNOWN);             // 빈 토큰
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.TOKEN_UNKNOWN);             // 이외의 모든 예외
        }
    }

    // 토큰 검증
    public String getloginId(String token) {

        return parseClaims(token).get("LoginId", String.class);
    }
}