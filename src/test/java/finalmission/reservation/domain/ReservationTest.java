package finalmission.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import finalmission.member.domain.Member;
import finalmission.reservation.exception.ReservationNotPendingException;
import finalmission.reservationtime.domain.ReservationTime;
import finalmission.restaurant.domain.Restaurant;
import finalmission.restaurant.exception.RestaurantNotOwnerException;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

class ReservationTest {

    @Nested
    @DisplayName("예약을 승인처리한다.")
    class Accept {

        @DisplayName("예약을 승인처리한다.")
        @Test
        void accept1() {
            // given
            final Member member = new Member(1L, "nickname", "asd@naver.com", "1234");
            final Restaurant restaurant = new Restaurant(1L, "restaurant", member);
            final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 30), restaurant);

            final Reservation reservation = Reservation.builder()
                    .id(1L)
                    .restaurant(restaurant)
                    .reservationTime(reservationTime)
                    .member(member)
                    .reservationState(ReservationState.PENDING)
                    .date(LocalDate.of(2025, 6, 12))
                    .build();

            // when
            reservation.accept(member);

            // then
            assertThat(reservation.getReservationState()).isSameAs(ReservationState.ACCEPT);
        }

        @DisplayName("예약 상태가 PENDING 상태가 아니라면 예외가 발생한다.")
        @EnumSource(mode = Mode.EXCLUDE, names = {"PENDING"})
        @ParameterizedTest
        void accept2(final ReservationState reservationState) {
            // given
            final Member member = new Member(1L, "nickname", "asd@naver.com", "1234");
            final Restaurant restaurant = new Restaurant(1L, "restaurant", member);
            final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 30), restaurant);

            final Reservation reservation = Reservation.builder()
                    .id(1L)
                    .restaurant(restaurant)
                    .reservationTime(reservationTime)
                    .member(member)
                    .reservationState(reservationState)
                    .date(LocalDate.of(2025, 6, 12))
                    .build();

            // when & then
            assertThatThrownBy(() -> {
                reservation.accept(member);
            }).isInstanceOf(ReservationNotPendingException.class);
        }

        @DisplayName("식당 소유주가 아니라면 예외가 발생한다.")
        @Test
        void accept3() {
            // given
            final Member member = new Member(1L, "nickname", "asd@naver.com", "1234");
            final Restaurant restaurant = new Restaurant(1L, "restaurant", member);
            final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 30), restaurant);

            final Reservation reservation = Reservation.builder()
                    .id(1L)
                    .restaurant(restaurant)
                    .reservationTime(reservationTime)
                    .member(member)
                    .reservationState(ReservationState.PENDING)
                    .date(LocalDate.of(2025, 6, 12))
                    .build();
            final Member anotherMember = new Member(2L, "nickname", "asd@naver.com", "1234");

            // when & then
            assertThatThrownBy(() -> {
                reservation.accept(anotherMember);
            }).isInstanceOf(RestaurantNotOwnerException.class);
        }
    }

    @Nested
    @DisplayName("예약을 거부처리한다.")
    class Reject {

        @DisplayName("예약을 거부처리한다.")
        @Test
        void accept1() {
            // given
            final Member member = new Member(1L, "nickname", "asd@naver.com", "1234");
            final Restaurant restaurant = new Restaurant(1L, "restaurant", member);
            final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 30), restaurant);

            final Reservation reservation = Reservation.builder()
                    .id(1L)
                    .restaurant(restaurant)
                    .reservationTime(reservationTime)
                    .member(member)
                    .reservationState(ReservationState.PENDING)
                    .date(LocalDate.of(2025, 6, 12))
                    .build();

            // when
            reservation.reject(member);

            // then
            assertThat(reservation.getReservationState()).isSameAs(ReservationState.REJECT);
        }

        @DisplayName("예약 상태가 PENDING 상태가 아니라면 예외가 발생한다.")
        @EnumSource(mode = Mode.EXCLUDE, names = {"PENDING"})
        @ParameterizedTest
        void accept2(final ReservationState reservationState) {
            // given
            final Member member = new Member(1L, "nickname", "asd@naver.com", "1234");
            final Restaurant restaurant = new Restaurant(1L, "restaurant", member);
            final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 30), restaurant);

            final Reservation reservation = Reservation.builder()
                    .id(1L)
                    .restaurant(restaurant)
                    .reservationTime(reservationTime)
                    .member(member)
                    .reservationState(reservationState)
                    .date(LocalDate.of(2025, 6, 12))
                    .build();

            // when & then
            assertThatThrownBy(() -> {
                reservation.reject(member);
            }).isInstanceOf(ReservationNotPendingException.class);
        }

        @DisplayName("식당 소유주가 아니라면 예외가 발생한다.")
        @Test
        void accept3() {
            // given
            final Member member = new Member(1L, "nickname", "asd@naver.com", "1234");
            final Restaurant restaurant = new Restaurant(1L, "restaurant", member);
            final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 30), restaurant);

            final Reservation reservation = Reservation.builder()
                    .id(1L)
                    .restaurant(restaurant)
                    .reservationTime(reservationTime)
                    .member(member)
                    .reservationState(ReservationState.PENDING)
                    .date(LocalDate.of(2025, 6, 12))
                    .build();
            final Member anotherMember = new Member(2L, "nickname", "asd@naver.com", "1234");

            // when & then
            assertThatThrownBy(() -> {
                reservation.reject(anotherMember);
            }).isInstanceOf(RestaurantNotOwnerException.class);
        }
    }

    @Nested
    @DisplayName("Member가 예약을 신청한 멤버인가?")
    class IsOwner {

        @DisplayName("멤버가 예약을 신청한 멤버라면 true를 반환한다.")
        @Test
        void isOwner1() {
            // given
            final Member member = new Member(1L, "nickname", "asd@naver.com", "1234");
            final Restaurant restaurant = new Restaurant(1L, "restaurant", member);
            final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 30), restaurant);

            final Reservation reservation = Reservation.builder()
                    .id(1L)
                    .restaurant(restaurant)
                    .reservationTime(reservationTime)
                    .member(member)
                    .reservationState(ReservationState.PENDING)
                    .date(LocalDate.of(2025, 6, 12))
                    .build();

            // when
            final boolean actual = reservation.isOwnMember(member);

            // then
            assertThat(actual).isTrue();
        }

        @DisplayName("멤버가 예약을 신청한 멤버가 아니라면 false를 반환한다.")
        @Test
        void isOwner2() {
            // given
            final Member member = new Member(1L, "nickname", "asd@naver.com", "1234");
            final Restaurant restaurant = new Restaurant(1L, "restaurant", member);
            final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 30), restaurant);

            final Reservation reservation = Reservation.builder()
                    .id(1L)
                    .restaurant(restaurant)
                    .reservationTime(reservationTime)
                    .member(member)
                    .reservationState(ReservationState.PENDING)
                    .date(LocalDate.of(2025, 6, 12))
                    .build();
            final Member anotherMember = new Member(2L, "nickname", "asd@naver.com", "1234");

            // when
            final boolean actual = reservation.isOwnMember(anotherMember);

            // then
            assertThat(actual).isFalse();
        }
    }

}
