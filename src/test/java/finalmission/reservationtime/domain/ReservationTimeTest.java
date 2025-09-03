package finalmission.reservationtime.domain;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.member.domain.Member;
import finalmission.restaurant.domain.Restaurant;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Nested
    @DisplayName("예약 시간이 주어진 식당의 예약 시간인가?")
    class IsEqualRestaurant {

        @DisplayName("예약 시간이 주어진 식당의 예약 시간이라면 true를 반환한다.")
        @Test
        void isEqualRestaurant1() {
            // given
            final Member member = new Member(1L, "nickname", "asd@naver.com", "1234");
            final Restaurant restaurant = new Restaurant(1L, "restaurant", member);

            final ReservationTime reservationTime = ReservationTime.builder()
                    .id(1L)
                    .restaurant(restaurant)
                    .time(LocalTime.of(12, 30))
                    .build();

            // when
            final boolean actual = reservationTime.isEqualRestaurant(restaurant);

            // then
            assertThat(actual).isTrue();
        }

        @DisplayName("예약 시간이 주어진 식당의 예약 시간이 아니라면 false를 반환한다.")
        @Test
        void isEqualRestaurant2() {
            // given
            final Member member = new Member(1L, "nickname", "asd@naver.com", "1234");
            final Restaurant restaurant = new Restaurant(1L, "restaurant", member);

            final ReservationTime reservationTime = ReservationTime.builder()
                    .id(1L)
                    .restaurant(restaurant)
                    .time(LocalTime.of(12, 30))
                    .build();
            final Restaurant anotherRestaurant = new Restaurant(2L, "rest2", member);

            // when
            final boolean actual = reservationTime.isEqualRestaurant(anotherRestaurant);

            // then
            assertThat(actual).isFalse();
        }

    }

}
