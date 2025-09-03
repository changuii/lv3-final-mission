package finalmission.reservation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ReservationRequest (
        @NotNull LocalDate date,
        @Positive Long reservationTimeId,
        @Positive Long restaurantId
){
}
