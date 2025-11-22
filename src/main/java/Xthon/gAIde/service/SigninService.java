package Xthon.gAIde.service;

import Xthon.gAIde.domain.dto.request.SigninRequestDto;
import Xthon.gAIde.domain.dto.response.SigninResponseDto;
import Xthon.gAIde.domain.entity.MemberEntity;
import Xthon.gAIde.exception.CustomException;
import Xthon.gAIde.exception.ErrorCode;
import Xthon.gAIde.repository.MemberRepository;
import Xthon.gAIde.util.common.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SigninService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 유저 로그인 로직
    @Transactional
    public SigninResponseDto signin(SigninRequestDto signinRequestDto) {

        // loginId로 entity 찾기
        MemberEntity memberEntity = memberRepository.findByLoginId(signinRequestDto.loginId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));


        // 비밀번호 불일치
        if (!passwordEncoder.matches(signinRequestDto.password(), memberEntity.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 발급
        String accessToken = jwtUtil.createJwt(signinRequestDto.loginId());

        return new SigninResponseDto(accessToken);
    }
}
