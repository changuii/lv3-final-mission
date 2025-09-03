package finalmission.restaurant.controller;

import finalmission.config.AuthenticationPrincipal;
import finalmission.restaurant.dto.RestaurantRequest;
import finalmission.restaurant.dto.RestaurantResponse;
import finalmission.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(
            @AuthenticationPrincipal final String email,
            @Valid @RequestBody final RestaurantRequest request
    ) {
        final RestaurantResponse response = restaurantService.createRestaurant(request, email);
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        return ResponseEntity.ok(restaurantService.findAll());
    }


}
