package finalmission.reservationtime.exception;

import finalmission.exception.custom.ConflictException;

public class ReservationTimeDuplicationException extends ConflictException {

    public ReservationTimeDuplicationException() {
        super("예약 시간이 이미 존재합니다.");
    }
}
