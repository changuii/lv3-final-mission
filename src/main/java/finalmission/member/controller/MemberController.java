package finalmission.member.controller;

import finalmission.member.dto.MemberRequest;
import finalmission.member.dto.MemberResponse;
import finalmission.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(
            @Valid @RequestBody final MemberRequest request
    ) {
        final MemberResponse member = memberService.createMember(request);
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(member);
    }
}
