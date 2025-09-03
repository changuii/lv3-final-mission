package finalmission.member.exception;

import finalmission.exception.custom.BadRequestException;

public class MemberNotExistsException extends BadRequestException {

    public MemberNotExistsException() {
        super("존재하지 않는 멤버입니다.");
    }
}
