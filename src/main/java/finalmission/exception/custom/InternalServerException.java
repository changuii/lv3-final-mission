package finalmission.exception.custom;

import org.springframework.http.HttpStatus;

public class InternalServerException extends CustomException{

    public InternalServerException(final String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
