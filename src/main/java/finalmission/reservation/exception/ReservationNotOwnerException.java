package finalmission.reservation.exception;

import finalmission.exception.custom.UnauthorizedException;

public class ReservationNotOwnerException extends UnauthorizedException {

    public ReservationNotOwnerException() {
        super("예약에 접근할 수 없는 사용자입니다.");
    }
}
