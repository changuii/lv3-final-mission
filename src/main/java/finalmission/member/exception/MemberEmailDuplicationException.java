package finalmission.member.exception;

import finalmission.exception.custom.ConflictException;

public class MemberEmailDuplicationException extends ConflictException {

    public MemberEmailDuplicationException() {
        super("member의 이메일이 이미 존재합니다.");
    }
}
