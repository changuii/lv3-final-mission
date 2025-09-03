package finalmission.member.exception;

import finalmission.exception.custom.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException() {
        super("존재하지 않는 멤버입니다.");
    }
}
