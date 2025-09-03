package finalmission.restaurant.dto;

import jakarta.validation.constraints.NotBlank;

public record RestaurantRequest(
        @NotBlank String name
) {
}
