package finalmission.reservation.domain;

import lombok.Getter;

@Getter
public enum ReservationState {

    PENDING("승인 대기중"),
    ACCEPT("승인"),
    REJECT("거부");

    private final String message;

    ReservationState(final String message) {
        this.message = message;
    }
}
