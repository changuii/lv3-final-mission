package finalmission.reservation.exception;

import finalmission.exception.custom.BadRequestException;

public class ReservationPastException extends BadRequestException {

    public ReservationPastException() {
        super("지난 시간 혹은 당일에는 예약이 불가능합니다.");
    }
}
