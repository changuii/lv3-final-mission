package finalmission.exception.custom;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException{

    public BadRequestException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
