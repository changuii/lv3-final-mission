package finalmission.member.controller;

import finalmission.member.dto.AuthRequest;
import finalmission.member.dto.AuthResponse;
import finalmission.member.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody final AuthRequest request
    ) {
        final AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
