package finalmission.member.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtAuthTokenProviderTest {

    private final JwtAuthTokenProvider tokenProvider;

    public JwtAuthTokenProviderTest(){
        this.tokenProvider = new JwtAuthTokenProvider(
                "privateKeyprivateKeyprivateKeyprivateKeyprivateKey",
                3600
        );
    }

    @DisplayName("Bearer 접두사가 붙은 토큰이 발급된다.")
    @Test
    void generateToken() {
        // given
        final String email = "email";

        // when
        final String actual = tokenProvider.generateToken(email);

        // then
        assertThat(actual)
                .startsWith("Bearer ");

    }
}
