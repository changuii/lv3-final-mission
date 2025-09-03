package finalmission.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.reservationtime.domain.ReservationTime;
import java.time.LocalTime;

public record ReservationTimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm") LocalTime time
) {

    public static ReservationTimeResponse from(final ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getTime()
        );
    }
}
