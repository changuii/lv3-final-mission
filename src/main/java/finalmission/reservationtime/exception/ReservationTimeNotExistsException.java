package finalmission.reservationtime.exception;

import finalmission.exception.custom.BadRequestException;

public class ReservationTimeNotExistsException extends BadRequestException {
    public ReservationTimeNotExistsException() {
        super("존재하지 않는 예약 시간입니다.");
    }
}
