package finalmission.exception.custom;

import org.springframework.http.HttpStatus;

public class ConflictException extends CustomException{

    public ConflictException(final String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
