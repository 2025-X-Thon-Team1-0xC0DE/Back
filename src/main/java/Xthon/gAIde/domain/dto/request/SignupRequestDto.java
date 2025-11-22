package Xthon.gAIde.domain.dto.request;

import jakarta.validation.constraints.NotNull;

public record SignupRequestDto(
        @NotNull(message = "아이디(loginId)는 필수 입력값입니다.")
        String loginId,

        @NotNull(message = "비밀번호(password)는 필수 입력값입니다.")
        String password,

        @NotNull(message = "이름(name)은 필수 입력값입니다.")
        String name
) {
}
