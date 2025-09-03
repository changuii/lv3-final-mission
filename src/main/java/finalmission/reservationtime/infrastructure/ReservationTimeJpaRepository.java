package finalmission.reservationtime.infrastructure;

import finalmission.reservationtime.domain.ReservationTime;
import finalmission.restaurant.domain.Restaurant;
import java.time.LocalTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationTimeJpaRepository extends JpaRepository<ReservationTime, Long> {

    boolean existsByTimeAndRestaurant(LocalTime time, Restaurant restaurant);
}
