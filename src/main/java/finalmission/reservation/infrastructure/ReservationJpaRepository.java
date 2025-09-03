package finalmission.reservation.infrastructure;

import finalmission.member.domain.Member;
import finalmission.reservation.domain.Reservation;
import finalmission.restaurant.domain.Restaurant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"reservationTime", "member", "restaurant"})
    List<Reservation> findAllByMember(final Member member);

    @EntityGraph(attributePaths = {"reservationTime", "member", "restaurant"})
    List<Reservation> findAllByRestaurantAndDate(final Restaurant restaurant, final LocalDate date);
}
