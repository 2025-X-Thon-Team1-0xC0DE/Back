package Xthon.gAIde.util.common;

import Xthon.gAIde.util.common.security.CustomUserDetailsService; // 경로 확인 필요
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import Xthon.gAIde.exception.CustomException;
import Xthon.gAIde.exception.ErrorCode;
import Xthon.gAIde.util.common.CommonResponseDto; // DTO 경로 확인

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    // ★ 추가: 유저 정보를 DB에서 가져오기 위해 필요
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.equals("/api/signin") || path.equals("/api/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            // 1. 토큰 검증 (만료 여부 등 체크) - JwtUtil에 validateToken이 있다면 여기서 체크 권장
            // if (!jwtUtil.validateToken(token)) throw ...

            // 2. 토큰에서 ID 추출
            String loginId = jwtUtil.getloginId(token);

            // 3. ★핵심 수정★: DB에서 UserDetails(CustomUserDetails) 객체를 직접 로드
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);

            // 4. Principal 자리에 'userDetails 객체'를 넣어야 컨트롤러에서 받을 수 있음
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,         // Principal (여기에 객체가 들어가야 함)
                            null,                // Credentials (비번은 null 처리)
                            userDetails.getAuthorities() // 권한 정보
                    );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (CustomException e) {
            log.warn("JwtFilter Caught CustomException: {}", e.getErrorCode().getMessage());
            setErrorResponse(response, e.getErrorCode());
        } catch (Exception e) {
            // 기타 예외 처리 (예: loadUserByUsername 실패 등)
            log.error("Authentication Error: {}", e.getMessage());
            filterChain.doFilter(request, response); // 혹은 에러 응답
        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            ErrorCode errorCode
    ) throws IOException {
        CommonResponseDto<?> errorResponse = CommonResponseDto.fail(new CustomException(errorCode));
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}