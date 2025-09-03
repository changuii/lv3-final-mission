package finalmission.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import finalmission.exception.custom.CustomException;
import finalmission.member.domain.AuthTokenProvider;
import finalmission.member.domain.Member;
import finalmission.member.dto.AuthRequest;
import finalmission.member.dto.AuthResponse;
import finalmission.member.infrastructure.MemberJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AuthServiceTest {

    private final AuthService authService;
    private final AuthTokenProvider authTokenProvider;
    private final MemberJpaRepository memberJpaRepository;

    public AuthServiceTest() {
        this.authTokenProvider = mock(AuthTokenProvider.class);
        this.memberJpaRepository = mock(MemberJpaRepository.class);

        this.authService = new AuthService(memberJpaRepository, authTokenProvider);
    }

    @Nested
    @DisplayName("login")
    class Login {

        @DisplayName("정상 로그인 테스트")
        @Test
        void login1() {
            // given
            final AuthRequest authRequest = new AuthRequest(
                    "asd123@naver.com",
                    "password"
            );

            given(memberJpaRepository.findByEmail(authRequest.email()))
                    .willReturn(Optional.of(
                            new Member(1L, "mock", authRequest.email(), authRequest.password())
                    ));
            given(authTokenProvider.generateToken(authRequest.email()))
                    .willReturn("token");

            final AuthResponse expected = new AuthResponse(
                    "token"
            );

            // when
            final AuthResponse actual = authService.login(authRequest);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("유저가 존재하지 않으면 예외가 발생한다.")
        @Test
        void login2() {
            // given
            final AuthRequest authRequest = new AuthRequest(
                    "asd123@naver.com",
                    "password"
            );

            given(memberJpaRepository.findByEmail(authRequest.email()))
                    .willReturn(Optional.empty());
            given(authTokenProvider.generateToken(authRequest.email()))
                    .willReturn("token");

            // when & then
            assertThatThrownBy(() -> {
                authService.login(authRequest);
            }).isInstanceOf(CustomException.class);
        }

        @DisplayName("유저의 비밀번호가 일치하지 않으면 예외가 발생한다.")
        @Test
        void login3() {
            // given
            final AuthRequest authRequest = new AuthRequest(
                    "asd123@naver.com",
                    "password"
            );

            given(memberJpaRepository.findByEmail(authRequest.email()))
                    .willReturn(Optional.of(
                            new Member(1L, "mock", authRequest.email(), "is not match pass")
                    ));
            given(authTokenProvider.generateToken(authRequest.email()))
                    .willReturn("token");

            // when & then
            assertThatThrownBy(() -> {
                authService.login(authRequest);
            }).isInstanceOf(CustomException.class);
        }
    }

}
