package Xthon.gAIde.service;

import Xthon.gAIde.domain.dto.request.SignupRequestDto;
import Xthon.gAIde.domain.entity.MemberEntity;
import Xthon.gAIde.exception.CustomException;
import Xthon.gAIde.exception.ErrorCode;
import Xthon.gAIde.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SignupService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 멤버 회원가입 로직
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {

        // 아이디 중복 체크
        if (memberRepository.existsByLoginId(signupRequestDto.loginId())) {
            throw new CustomException(ErrorCode.CONFLICT_MEMBER_ID);
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.password());

        MemberEntity member = MemberEntity.builder()
                .loginId(signupRequestDto.loginId())
                .password(encodedPassword)
                .name(signupRequestDto.name())
                .build();

        memberRepository.save(member);
    }
}
