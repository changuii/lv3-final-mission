package finalmission.reservationtime.controller;

import finalmission.config.AuthenticationPrincipal;
import finalmission.reservationtime.dto.ReservationTimeRequest;
import finalmission.reservationtime.dto.ReservationTimeResponse;
import finalmission.reservationtime.service.ReservationTimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation-time")
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @AuthenticationPrincipal final String email,
            @Valid @RequestBody final ReservationTimeRequest request
    ) {
        final ReservationTimeResponse response = reservationTimeService.create(request, email);
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(response);
    }
}
