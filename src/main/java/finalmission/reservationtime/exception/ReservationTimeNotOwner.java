package finalmission.reservationtime.exception;

import finalmission.exception.custom.UnauthorizedException;

public class ReservationTimeNotOwner extends UnauthorizedException {

    public ReservationTimeNotOwner() {
        super("예약 시간에 접근할 수 없는 사용자입니다.");
    }
}
