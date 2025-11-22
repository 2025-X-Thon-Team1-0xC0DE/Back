package Xthon.gAIde.controller;

import Xthon.gAIde.domain.dto.request.SigninRequestDto;
import Xthon.gAIde.domain.dto.request.SignupRequestDto;
import Xthon.gAIde.service.SigninService;
import Xthon.gAIde.service.SignupService;
import Xthon.gAIde.util.common.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final SignupService signupService;
    private final SigninService signinService;

    // 유저 회원가입
    @PostMapping("/signup")
    public CommonResponseDto<?> signup(@RequestBody SignupRequestDto signupRequestDto) {
        signupService.signup(signupRequestDto);
        return CommonResponseDto.created("유저 회원가입 성공");
    }

    @PostMapping("/signin")
    public CommonResponseDto<?> signin(@RequestBody SigninRequestDto signinRequestDto) {
        return CommonResponseDto.ok(signinService.signin(signinRequestDto));
    }
}
