package finalmission.member.exception;

import finalmission.exception.custom.InternalServerException;

public class MemberNicknameException extends InternalServerException {

    public MemberNicknameException() {
        super("닉네임 생성에 실패하였습니다. 잠시 후 재시도해주세요.");
    }
}
