package finalmission.member.exception;

import finalmission.exception.custom.BadRequestException;

public class MemberPasswordMissMatchException extends BadRequestException {

    public MemberPasswordMissMatchException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
