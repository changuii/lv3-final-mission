package finalmission.reservation.exception;

import finalmission.exception.custom.BadRequestException;

public class ReservationNotExistsException extends BadRequestException {

    public ReservationNotExistsException() {
        super("존재하지 않는 예약입니다.");
    }
}
