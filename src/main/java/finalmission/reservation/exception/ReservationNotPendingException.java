package finalmission.reservation.exception;

import finalmission.exception.custom.BadRequestException;

public class ReservationNotPendingException extends BadRequestException {

    public ReservationNotPendingException() {
        super("PENDING 상태에만 승인 혹은 거부처리할 수 있습니다.");
    }
}
