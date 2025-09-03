package finalmission.restaurant.exception;

import finalmission.exception.custom.UnauthorizedException;

public class RestaurantNotOwnerException extends UnauthorizedException {

    public RestaurantNotOwnerException() {
        super("식당에 접근할 수 없는 사용자입니다.");
    }
}
