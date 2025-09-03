package finalmission.member.dto;

import finalmission.member.domain.Member;

public record MemberResponse(
        String email,
        String nickname
) {

    public static MemberResponse from(final Member member) {
        return new MemberResponse(
                member.getEmail(),
                member.getNickname()
        );
    }
}
