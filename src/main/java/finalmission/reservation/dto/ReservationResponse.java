package finalmission.reservation.dto;

import finalmission.reservation.domain.Reservation;
import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        LocalDate date,
        String state,
        Long timeId,
        String email,
        Long restaurantId
) {

    public static ReservationResponse from(final Reservation reservation, final String email){
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                reservation.getReservationState().getMessage(),
                reservation.getReservationTime().getId(),
                email,
                reservation.getRestaurant().getId()

        );
    }
}
