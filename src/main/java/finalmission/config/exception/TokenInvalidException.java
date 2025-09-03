package finalmission.config.exception;

import finalmission.exception.custom.UnauthorizedException;

public class TokenInvalidException extends UnauthorizedException {

    public TokenInvalidException() {
        super("유효하지 않은 토큰입니다.");
    }
}
