package finalmission.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import finalmission.exception.custom.CustomException;
import finalmission.member.domain.Member;
import finalmission.member.domain.NameGenerator;
import finalmission.member.dto.MemberRequest;
import finalmission.member.dto.MemberResponse;
import finalmission.member.dto.NicknameResult;
import finalmission.member.infrastructure.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MemberServiceTest {

    private final MemberService memberService;
    private final MemberJpaRepository memberJpaRepository;
    private final NameGenerator nameGenerator;

    public MemberServiceTest() {
        memberJpaRepository = mock(MemberJpaRepository.class);
        nameGenerator = mock(NameGenerator.class);
        this.memberService = new MemberService(memberJpaRepository, nameGenerator);
    }

    @Nested
    @DisplayName("member 생성")
    class CreateMember {

        @DisplayName("정상 생성 테스트")
        @Test
        void member1() {
            // given
            final MemberRequest request = new MemberRequest(
                    "asd123@naver.com",
                    "pass"
            );

            final String nickname = "mock";
            given(nameGenerator.generateName())
                    .willReturn(new NicknameResult(nickname, false));
            given(memberJpaRepository.save(any()))
                    .willReturn(new Member(
                            1L,
                            nickname,
                            request.email(),
                            request.password()
                    ));

            final MemberResponse expected = new MemberResponse(
                    "asd123@naver.com",
                    nickname
            );

            // when
            final MemberResponse actual = memberService.createMember(request);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("같은 이메일이 존재하면 예외가 발생한다.")
        @Test
        void member2() {
            // given
            final MemberRequest request = new MemberRequest(
                    "asd123@naver.com",
                    "pass"
            );

            final String nickname = "mock";
            given(nameGenerator.generateName())
                    .willReturn(new NicknameResult(nickname, false));
            given(memberJpaRepository.existsByEmail(request.email()))
                    .willReturn(true);
            given(memberJpaRepository.save(any()))
                    .willReturn(new Member(
                            1L,
                            "mock",
                            request.email(),
                            request.password()
                    ));

            // when & then
            assertThatThrownBy(() -> {
                memberService.createMember(request);
            }).isInstanceOf(CustomException.class);
        }
    }

}
