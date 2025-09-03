package finalmission.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MemberTest {

    @DisplayName("비밀번호가 일치하면 true 다르다면 false를 반환한다.")
    @CsvSource(value = {"password:true", "another password:false"}, delimiter = ':')
    @ParameterizedTest
    void matchPassword(final String password, final boolean expected) {
        // given
        final Member member = new Member(
                1L, "nickname", "email", "password"
        );

        // when
        final boolean actual = member.matchPassword(password);

        // then
        assertThat(actual).isEqualTo(expected);
    }

}
