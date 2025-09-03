package finalmission.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
        @Email String email,
        @NotBlank String password
) {
}
