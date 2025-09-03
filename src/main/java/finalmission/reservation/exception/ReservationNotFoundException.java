package finalmission.reservation.exception;

import finalmission.exception.custom.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {

    public ReservationNotFoundException() {
        super("예약이 존재하지 않습니다.");
    }
}
