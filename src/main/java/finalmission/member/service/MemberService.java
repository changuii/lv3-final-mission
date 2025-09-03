package finalmission.member.service;

import finalmission.member.domain.Member;
import finalmission.member.domain.NameGenerator;
import finalmission.member.dto.MemberRequest;
import finalmission.member.dto.MemberResponse;
import finalmission.member.dto.NicknameResult;
import finalmission.member.exception.MemberEmailDuplicationException;
import finalmission.member.exception.MemberNicknameException;
import finalmission.member.infrastructure.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final NameGenerator nameGenerator;

    public MemberResponse createMember(final MemberRequest request) {
        if (memberJpaRepository.existsByEmail(request.email())) {
            throw new MemberEmailDuplicationException();
        }
        final NicknameResult nicknameResult = nameGenerator.generateName();
        validateNickname(nicknameResult);

        final Member notSavedMember = Member.builder()
                .email(request.email())
                .password(request.password())
                .nickname(nicknameResult.nickname())
                .build();

        final Member savedMember = memberJpaRepository.save(notSavedMember);
        return MemberResponse.from(savedMember);
    }

    private void validateNickname(final NicknameResult nicknameResult) {
        if (nicknameResult.isError()) {
            throw new MemberNicknameException();
        }
    }
}
