package finalmission.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalTime;

public record ReservationTimeRequest(
        @Positive Long restaurantId,
        @NotNull @JsonFormat(pattern = "HH:mm") LocalTime time
) {
}
