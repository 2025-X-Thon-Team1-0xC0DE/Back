package Xthon.gAIde.util.common.security;

import Xthon.gAIde.domain.entity.MemberEntity;
import Xthon.gAIde.repository.MemberRepository; // 레포지토리 경로 확인 필요
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // 1. DB에서 loginId로 회원 조회
        MemberEntity member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디를 가진 유저를 찾을 수 없습니다: " + loginId));

        // 2. 찾은 회원 엔티티를 SecurityContext가 이해할 수 있는 UserDetails로 감싸서 반환
        return new CustomUserDetails(member);
    }
}