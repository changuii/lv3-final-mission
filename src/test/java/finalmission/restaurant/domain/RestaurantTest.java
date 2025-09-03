package finalmission.restaurant.domain;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RestaurantTest {

    @Nested
    @DisplayName("식당이 주어진 멤버의 소유인가?")
    class IsOwner {

        @DisplayName("식당이 주어진 멤버의 소유라면 true를 반환한다.")
        @Test
        void isOwner1() {
            // given
            final Member member = new Member(1L, "nickname", "asd@naver.com", "1234");

            final Restaurant restaurant = Restaurant.builder()
                    .id(1L)
                    .name("restaurant")
                    .member(member)
                    .build();

            // when
            final boolean actual = restaurant.isOwner(member);

            // then
            assertThat(actual).isTrue();
        }

        @DisplayName("식당이 주어진 멤버의 소유가 아니라면 false를 반환한다.")
        @Test
        void isOwner2() {
            // given
            final Member member = new Member(1L, "nickname", "asd@naver.com", "1234");

            final Restaurant restaurant = Restaurant.builder()
                    .id(1L)
                    .name("restaurant")
                    .member(member)
                    .build();
            final Member anotherMember = new Member(2L, "nickname", "asd@naver.com", "1234");

            // when
            final boolean actual = restaurant.isOwner(anotherMember);

            // then
            assertThat(actual).isFalse();
        }
    }

}
