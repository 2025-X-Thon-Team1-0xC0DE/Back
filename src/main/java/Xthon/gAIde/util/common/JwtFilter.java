package Xthon.gAIde.util.common;

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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import Xthon.gAIde.exception.CustomException;
import Xthon.gAIde.exception.ErrorCode;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 검사 예외 경로
        String path = request.getRequestURI();
        if (path.equals("/api/signin") || path.equals("/api/signout") ||
                path.equals("/mcp")) {

            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 토큰을 꺼냄
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("Authorization 헤더가 없거나 'Bearer' 타입이 아닙니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 순수토큰 추출 == Barrer  잘라내기
        String token = authorizationHeader.substring(7);

        // 성공 시 인증 정보 등록 후 다음 필터로 넘어감, 실패 시 커스텀익셉션
        try {
            String loginId = jwtUtil.getloginId(token);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginId, null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (CustomException e) {
            log.warn("JwtFilter Caugh CustomException: {}", e.getErrorCode().getMessage());
            setErrorResponse(response, e.getErrorCode());
        }
    }

    // 필터에서 잡은 예외를 CommonResponseDto JSON으로 만들어 응답
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