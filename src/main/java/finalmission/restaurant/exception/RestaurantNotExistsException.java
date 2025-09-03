package finalmission.restaurant.exception;

import finalmission.exception.custom.BadRequestException;

public class RestaurantNotExistsException extends BadRequestException {

    public RestaurantNotExistsException() {
        super("존재하지않는 식당입니다.");
    }
}
